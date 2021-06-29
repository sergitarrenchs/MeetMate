package com.lasalle.meet.enities;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.lasalle.meet.HomeScreen;
import com.lasalle.meet.exceptions.eventexceptions.EventDateStartAfterEndException;
import com.lasalle.meet.exceptions.eventexceptions.EventDateStartBeforeNowException;
import com.lasalle.meet.exceptions.eventexceptions.EventEndDateInvalidException;
import com.lasalle.meet.exceptions.eventexceptions.EventException;
import com.lasalle.meet.exceptions.eventexceptions.EventInvalidCredentialsException;
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

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Event implements Serializable {

    @SerializedName("id")
    @Expose(serialize = false, deserialize = true)
    private int ID;
    @Expose(serialize = true, deserialize = true)
    private String name;
    @Expose(serialize = false, deserialize = true)
    private int owner_id;
    @Expose(serialize = false, deserialize = true)
    private String date;
    @Expose(serialize = true, deserialize = true)
    private String image;
    @Expose(serialize = true, deserialize = true)
    private String location;
    @Expose(serialize = true, deserialize = true)
    private String description;
    @Expose(serialize = true, deserialize = true)
    private String eventStart_date;
    @Expose(serialize = true, deserialize = true)
    private String eventEnd_date;
    @Expose(serialize = true, deserialize = true)
    private int n_participators;
    @Expose(serialize = true, deserialize = true)
    private String type;
    @SerializedName("slug")
    @Expose(serialize = false, deserialize = true)
    private String comentary;
    @Expose(serialize = false, deserialize = true)
    private int punctuation;

    @Expose(serialize = false, deserialize = false)
    private int eventError;

    private final static int NO_ERROR = 0;
    private final static int BAD_REQUEST = 400;


    public Event(String name, String location, String description, Date eventStart_date, Date eventEnd_date, int n_participators, String type, String image) {
        this.name = name;
        this.image = image;
        this.location = location;
        this.description = description;
        this.eventStart_date = ISO8601Utils.format(eventStart_date, true);
        this.eventEnd_date = ISO8601Utils.format(eventEnd_date, true);
        this.n_participators = n_participators;
        this.type = type;
        this.image = image;
    }

    public static void createNewEvent(String name, String location, String description, Date eventStart_date, Date eventEnd_date, int n_participators, String type, String image, String accessToken) throws EventException {

        if (location.equals("")) {
            throw new EventNullLocationException();
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
                throw new EventInvalidCredentialsException();
            }

        } catch (InterruptedException e) {
            throw new EventInvalidCredentialsException();
        }
    }

    public static void newEventCheck(String name, String description, String eventStart_date, String eventEnd_date, String n_participators, String type, boolean checkCurrentDate) throws EventException{
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
            startDate = new SimpleDateFormat("dd / MM / yyyy HH : mm").parse(eventStart_date);
        } catch (ParseException e) {
            throw new EventStartDateInvalidException();
        }

        try {
            endDate = new SimpleDateFormat("dd / MM / yyyy HH : mm").parse(eventEnd_date);
        } catch (ParseException e) {
            throw new EventEndDateInvalidException();
        }


        if (checkCurrentDate) {
            if (startDate.before(Date.from(LocalDateTime.ofInstant(new Date().toInstant(),
                    ZoneId.systemDefault()).atZone(ZoneId.systemDefault()).toInstant()))){
                throw new EventDateStartBeforeNowException();
            } else if (startDate.after(endDate)) {
                throw new EventDateStartAfterEndException();
            }
        } else {
            if (startDate.after(endDate)) {
                throw new EventDateStartAfterEndException();
            }
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

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getEventStart_date() {
        SimpleDateFormat spf = new SimpleDateFormat("dd / MM / yyyy HH : mm");

        try {
            Date x = ISO8601Utils.parse(eventStart_date, new ParsePosition(0));
            String u = spf.format(x);

            return u;
        } catch (ParseException e) {
            return "No date";
        } catch (NullPointerException e) {
            return "No date";
        }
    }

    public String getEventEnd_date() {
        SimpleDateFormat spf = new SimpleDateFormat("dd / MM / yyyy HH : mm");

        try {
            Date x = ISO8601Utils.parse(eventEnd_date, new ParsePosition(0));
            String u = spf.format(x);

            return u;
        } catch (ParseException e) {
            return "No date";
        } catch (NullPointerException e) {
            return "No date";
        }
    }

    public int getN_participators() {
        return n_participators;
    }

    public Date getStartDateObj () {
        try {
            Date x = ISO8601Utils.parse(eventStart_date, new ParsePosition(0));
            return x;
        } catch (ParseException e) {
            return null;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public Date getEndDateObj () {
        try {
            Date x = ISO8601Utils.parse(eventEnd_date, new ParsePosition(0));
            return x;
        } catch (ParseException e) {
            return null;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public void eventAssist(String accessToken) throws EventInvalidCredentialsException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        eventError = 0;

        Call<Event> call = APIAdapter.getApiService().postUserAssistanInfo(ID, 0,"", "Bearer " + accessToken);

        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (!response.isSuccessful()) {
                    eventError = response.code();
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
            if (eventError == BAD_REQUEST) {
                throw new EventInvalidCredentialsException();
            }

        } catch (InterruptedException e) {
            throw new EventInvalidCredentialsException();
        }
    }

    public int getOwner_id() {
        return owner_id;
    }

    public int getID() {
        return ID;
    }

    public String getImage() {
        if (image == null) {
            return "";
        }
        return image;
    }

    public String getDate() {
        SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        try {
            Date x = ISO8601Utils.parse(eventStart_date, new ParsePosition(0));
            String u = spf.format(x);

            return u;
        } catch (ParseException e) {
            return "No date";
        } catch (NullPointerException e) {
            return "No date";
        }
    }

    public String getType() {
        return type;
    }

    public void deleteEvent(String accessToken) {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        eventError = 0;

        Call<Event> call = APIAdapter.getApiService().deleteEvent(ID,"Bearer " + accessToken);

        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (!response.isSuccessful()) {
                    eventError = response.code();
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
            if (eventError == BAD_REQUEST) {
                //TODO: Throw Exception Event Incorrect Error
            }

        } catch (InterruptedException e) {
            //TODO: Throw Exception Event Incorrect Error
        }
    }

    public void updateEvent(String name, String location, String description, Date eventStart_date, Date eventEnd_date, int n_participators, String type, String image,String accessToken) throws EventException {

        if (location.equals("")) {
            throw new EventNullLocationException();
        } else if (image.equals("")) {
            throw new EventNullImageException();
        }

        this.name = name;
        this.image = image;
        this.location = location;
        this.description = description;
        this.eventStart_date = ISO8601Utils.format(eventStart_date, true);
        this.eventEnd_date = ISO8601Utils.format(eventEnd_date, true);
        this.n_participators = n_participators;
        this.type = type;
        this.image = image;

        CountDownLatch countDownLatch = new CountDownLatch(1);

        eventError = 0;

        Call<Event> call = APIAdapter.getApiService().modifyEvent(ID,this,"Bearer " + accessToken);

        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (!response.isSuccessful()) {
                    eventError = response.code();
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
            if (eventError == BAD_REQUEST) {
                throw new EventInvalidCredentialsException();
            }

        } catch (InterruptedException e) {
            throw new EventInvalidCredentialsException();
        }
    }
}
