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
            startDate = new SimpleDateFormat("dd / MM / yyyy HH : mm").parse(eventStart_date);
        } catch (ParseException e) {
            throw new EventStartDateInvalidException();
        }

        try {
            endDate = new SimpleDateFormat("dd / MM / yyyy HH : mm").parse(eventEnd_date);
        } catch (ParseException e) {
            throw new EventEndDateInvalidException();
        }


        if (startDate.before(Date.from(LocalDateTime.ofInstant(new Date().toInstant(),
                ZoneId.systemDefault()).atZone(ZoneId.systemDefault()).toInstant()))){
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

    public static List<Event> getAllEvents(String accessToken){

//        CountDownLatch countDownLatch = new CountDownLatch(1);
//
//        Call<List<Event>> call = APIAdapter.getApiService().getEvent("","Bearer " + accessToken);
//
//        List<Event> myList;
//
//        call.enqueue(new Callback<List<Event>>() {
//            @Override
//            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
//                if (response.isSuccessful()){
//                    myList = response.body();
//                    countDownLatch.countDown();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Event>> call, Throwable t) {
//                countDownLatch.countDown();
//            }
//        });
//
//        try {
//            countDownLatch.await();
////            if (event.getEventError() == BAD_REQUEST) {
////                //TODO: Throw Exception Event Incorrect Error
////            }
//
//        } catch (InterruptedException e) {
//            //TODO: Throw Exception Event Incorrect Error
//        }
//
//        return myList;
        return null;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public static GoogleMap displayAllEventMarkers(List<Event> eventList, GoogleMap mMap, Context context) {
        mMap.clear();

        for (int i = 0; i < eventList.size(); i++) {
            Event event = (Event) eventList.get(0);
            List<Address> addresses;

            try {

                addresses = new Geocoder(context).getFromLocationName(event.getLocation(),1);

                if (addresses.size() > 0) {
                    Address location = addresses.get(0);

                    mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
                }

            } catch (IOException e) {

            }
        }

        return mMap;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public String getDate() {
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date x = ISO8601Utils.parse(eventEnd_date, new ParsePosition(0));
            String u = spf.format(x);

            return u;
        } catch (ParseException e) {
            return "No date";
        }
    }

    public String getType() {
        return type;
    }
}
