package com.lasalle.meet.enities;

import com.google.gson.annotations.Expose;
import com.lasalle.meet.exceptions.eventexceptions.EventDateStartAfterEndException;
import com.lasalle.meet.exceptions.eventexceptions.EventDateStartBeforeNowException;
import com.lasalle.meet.exceptions.eventexceptions.EventEndDateInvalidException;
import com.lasalle.meet.exceptions.eventexceptions.EventException;
import com.lasalle.meet.exceptions.eventexceptions.EventInvalidNumberException;
import com.lasalle.meet.exceptions.eventexceptions.EventNameNullException;
import com.lasalle.meet.exceptions.eventexceptions.EventNullDescriptionException;
import com.lasalle.meet.exceptions.eventexceptions.EventNullFinishException;
import com.lasalle.meet.exceptions.eventexceptions.EventNullImageException;
import com.lasalle.meet.exceptions.eventexceptions.EventNullLocationException;
import com.lasalle.meet.exceptions.eventexceptions.EventNullNumberException;
import com.lasalle.meet.exceptions.eventexceptions.EventNullStartDateException;
import com.lasalle.meet.exceptions.eventexceptions.EventNullTypeException;
import com.lasalle.meet.exceptions.eventexceptions.EventStartDateInvalidException;
import com.lasalle.meet.exceptions.userexceptions.UserEmailNullException;
import com.lasalle.meet.exceptions.userexceptions.UserPasswordNotEqualException;
import com.lasalle.meet.exceptions.userexceptions.UserPasswordNullException;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Event implements Serializable {

    @Expose(serialize = false, deserialize = false)
    private int ID;
    @Expose(serialize = true, deserialize = true)
    private String name;
    @Expose(serialize = false, deserialize = false)
    private int owner_id;
    @Expose(serialize = true, deserialize = true)
    private Date date;
    @Expose(serialize = true, deserialize = true)
    private String image;
    @Expose(serialize = true, deserialize = true)
    private String location;
    @Expose(serialize = true, deserialize = true)
    private String description;
    @Expose(serialize = true, deserialize = true)
    private Date eventStart_date;
    @Expose(serialize = true, deserialize = true)
    private Date eventEnd_date;
    @Expose(serialize = true, deserialize = true)
    private int n_participators;
    @Expose(serialize = true, deserialize = true)
    private String type;
    @Expose(serialize = false, deserialize = false)
    private String comentary;
    @Expose(serialize = false, deserialize = false)
    private int punctuation;

    @Expose(serialize = false, deserialize = false)
    private int eventError;

    private final static int NO_ERROR = 0;
    private final static int BAD_REQUEST = 400;


    public Event(String name, String location, String description, Date eventStart_date, Date eventEnd_date, int n_participators, String type, String image) {
        this.name = name;
        this.date = Date.from(Instant.from(LocalDateTime.now()));
        this.image = image;
        this.location = location;
        this.description = description;
        this.eventStart_date = eventStart_date;
        this.eventEnd_date = eventEnd_date;
        this.n_participators = n_participators;
        this.type = type;
        this.image = image;
    }

    public static void createNewEvent(String name, String location, String description, Date eventStart_date, Date eventEnd_date, int n_participators, String type, String image, String accessToken) throws EventException {
        if (name.equals("")) {
            throw new EventNameNullException();
        } else if (location.equals("")) {
            throw new EventNullLocationException();
        } else if (description.equals("")) {
            throw new EventNullDescriptionException();
        } else if (eventStart_date == null) {
            throw new EventNullStartDateException();
        } else if (eventEnd_date == null) {
            throw new EventNullFinishException();
        } else if (n_participators < 0) {
            throw new EventNullNumberException();
        } else if (type.equals("")) {
            throw new EventNullTypeException();
        } else if (image.equals("")) {
            throw new EventNullImageException();
        }



        Event event = new Event(name, location, description, eventStart_date, eventEnd_date, n_participators, type, image);

        CountDownLatch countDownLatch = new CountDownLatch(1);

        Call<Event> call = APIAdapter.getApiService().postEvent(event, "Bearer " + accessToken);

        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (!response.isSuccessful()) {
                    event.setEventError(response.code());
                }
                countDownLatch.countDown();
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();
            if (event.getEventError() == BAD_REQUEST) {
                //TODO: Throw Exception Event Incorrect Error
            }

        } catch (InterruptedException e) {
            //TODO: Throw Exception Event Incorrect Error
        }
    }

    public static void newEventCheck(String name, String description, String eventStart_date, String eventEnd_date, String n_participators, String type) throws EventException{
        if (name.equals("")) {
            throw new EventNameNullException();
        } else if (description.equals("")) {
            throw new EventNullDescriptionException();
        } else if (eventStart_date.equals("")) {
            throw new EventNullStartDateException();
        } else if (eventEnd_date.equals("")) {
            throw new EventNullFinishException();
        } else if (n_participators.equals("")) {
            throw new EventNullNumberException();
        } else if (type.equals("")) {
            throw new EventNullTypeException();
        }

        Date startDate = null;
        Date endDate = null;

        try {
            startDate = new SimpleDateFormat("dd/MM/yyyy").parse(eventStart_date);
        } catch (ParseException e) {
            throw new EventStartDateInvalidException();
        }

        try {
            endDate = new SimpleDateFormat("dd/MM/yyyy").parse(eventEnd_date);
        } catch (ParseException e) {
            throw new EventEndDateInvalidException();
        }

        if (startDate.before(Date.from(Instant.from(LocalDateTime.now())))){
            throw new EventDateStartBeforeNowException();
        } else if (startDate.after(endDate)) {
            throw new EventDateStartAfterEndException();
        }

        try {
            Integer.parseInt(n_participators);
        } catch (NumberFormatException e) {
            throw new EventInvalidNumberException();
        }

    }

    public int getEventError() {
        return eventError;
    }

    public void setEventError(int eventError) {
        this.eventError = eventError;
    }
}
