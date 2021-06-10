package com.lasalle.meet.apiadapter;

import com.lasalle.meet.enities.Event;
import com.lasalle.meet.enities.User;

import kotlin.sequences.USequencesKt;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface APIService {
    @Headers("Content-Type: multipart/form-data")

    //LOGIN//
    @POST("users/login")
    Call<String> postLoginUser(@Field("email") String email, @Field("password") String password);

    //USERS//
    @POST("users")
    public Call<User> postCreateUser(@Body User user);
    @GET("users")
    Call<User> getUser();
    @GET("users/")
    Call<User> getUserID();
    @GET("users/search/?s=john")
    Call<User> searchUser(@Field("s") String s);
    @PUT("users")
    Call<User> modifyUser(@Body User user);
    @DELETE("users")
    Call<User> deleteUser();
    @GET("users/ID/friends")
    Call<Event> getUserFriend(@Field("ID") int ID);

    //EVENTS//
    @GET("users/ID/events")
    Call<Event> getUserEvent(@Field("ID") int ID);
    @GET("users/ID/events/future")
    Call<Event> getFutureUserEvent(@Field("ID") int ID);
    @GET("users/ID/events/finished")
    Call<Event> getFinishedUserEvent(@Field("ID") int ID);
    @GET("users/ID/events/current")
    Call<Event> getCurrentUserEvent(@Field("ID") int ID);
    @GET("users/ID/assistances")
    Call<Event> getUserAssistances(@Field("ID") int ID);
    @GET("users/ID/assistances/future")
    Call<Event> getFutureUserAssistances(@Field("ID") int ID);
    @GET("users/ID/assistances/finished")
    Call<Event> getFinishedUserAssistances(@Field("ID") int ID);

    //EVENT CRUD//
    @POST("events")
    Call<Event> postEvent(@Body Event event);
    @GET("events")
    Call<Event> getEvent(@Field("t") String type);
    @GET("events/ID")
    Call<Event> getEventID();
    @PUT("events/ID")
    Call<Event> modifyEvent(@Body Event event);
    @DELETE("events/ID")
    Call<Event> deleteEvent();

}
