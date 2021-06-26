package com.lasalle.meet.apiadapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lasalle.meet.enities.Event;
import com.lasalle.meet.enities.Message;
import com.lasalle.meet.enities.User;

import java.util.List;

import kotlin.sequences.USequencesKt;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    //LOGIN
    @Headers("Content-type: application/json")
    @FormUrlEncoded
    @POST("users/login")
    Call<String> postLoginUser(@Field("email") String email, @Field("password") String password);

    @Headers("Content-type: application/json")
    @POST("users/login")
    Call<User> postLoginUser(@Body User user);

    //USERS
    //@Headers("Content-Type: multipart/form-data") I want to change it
    @Headers("Content-type: application/json")
    @POST("users")
    public Call<User> postCreateUser(@Body User user);

//    @GET("users")
//    Call<User> getUser();
//    @GET("users/")
//    Call<User> getUserID();

    @Headers("Content-type: application/json")
    @GET("users/search/")
    Call<List<User>> searchUser(@Query("s") String s, @Header("Authorization") String Token);


    @Headers("Content-type: application/json")
    @PUT("users")
    Call<User> modifyUser(@Body User user, @Header("Authorization") String Token);

    @Headers("Content-type: application/json")
    @DELETE("users")
    Call<User> deleteUser(@Header("Authorization") String Token);

//    @GET("users/ID/friends")
//    Call<Event> getUserFriend(@Field("ID") int ID);

    //EVENTS//
    @Headers("Content-type: application/json")
    @GET("users/{ID}/events")
    Call<List<Event>> getUserEvent(@Path("ID") int ID,@Header("Authorization") String Token);
//    @GET("users/ID/events/future")
//    Call<Event> getFutureUserEvent(@Field("ID") int ID);
//    @GET("users/ID/events/finished")
//    Call<Event> getFinishedUserEvent(@Field("ID") int ID);
//    @GET("users/ID/events/current")
//    Call<Event> getCurrentUserEvent(@Field("ID") int ID);

    @GET("users/{ID}/assistances")
    Call<List<Event>> getUserAssistances(@Path("ID") int ID, @Header("Authorization") String Token);
//    @GET("users/ID/assistances/future")
//    Call<Event> getFutureUserAssistances(@Field("ID") int ID);
//    @GET("users/ID/assistances/finished")
//    Call<Event> getFinishedUserAssistances(@Field("ID") int ID);

    //EVENT CRUD//
    @Headers("Content-type: application/json")
    @POST("events")
    Call<Event> postEvent(@Body Event event, @Header("Authorization") String Token);
    @Headers("Content-type: application/json")
    @GET("events")
    Call<List<Event>> getEvent(@Query("t") String t, @Header("Authorization") String Token);
//    @GET("events/ID")
//    Call<Event> getEventID();
    @Headers("Content-type: application/json")
    @PUT("events/{ID}")
    Call<Event> modifyEvent(@Path("ID") int eventID, @Body Event event, @Header("Authorization") String Token);
    @Headers("Content-type: application/json")
    @DELETE("events/{ID}")
    Call<Event> deleteEvent(@Path("ID") int eventID, @Header("Authorization") String Token);


    //EVENT ASSISTENCES//
//    @GET("events/ID/assistances")
//    Call<Event> getEventAssistans(@Field("t") String type); //WHICH ARE THE PARAMETERS??//
//    @GET("events/ID/assistances/ID_USER")
//    Call<Event> getUserIDAssistan(@Field("t") String type); //WHICH ARE THE PARAMETERS??//
    @FormUrlEncoded
    @POST("events/{ID}/assistances")
    Call<Event> postUserAssistanInfo(@Path("ID") int eventID, @Field("puntuation") int puntuation, @Field("comentary") String comentary, @Header("Authorization") String Token);
//    @PUT("events/ID/assistances")
//    Call<Event> modifyUserAssistanInfo(@Field("ID") int eventID, @Field("puntuation") int puntuation, @Field("comentary") String comentary); //WHICH ARE THE PARAMETERS??//
//    @DELETE("events/ID/assistances")
//    Call<Event> deleteAssistance(@Field("ID") int eventID); //WHICH ARE THE PARAMETERS??//
//
    //FRIENDS//
    @GET("friends/requests")
    Call<List<User>> getFriendRequests(@Header("Authorization") String Token);
    @Headers("Content-type: application/json")
    @GET("friends")
    Call<List<User>> getFriends(@Header("Authorization") String Token);
    @POST("friends/{ID}")
    Call<User> postFriendRequest(@Path("ID") int user_id, @Header("Authorization") String Token);
    @PUT("friends/{ID}")
    Call<User> putFriendRequestAccept(@Path("ID") int user_id, @Header("Authorization") String Token);
    @DELETE("friends/{ID}")
    Call<User> deleteFriendRequest(@Path("ID") int user_id, @Header("Authorization") String Token);

    //MESSAGES//
    @Headers("Content-type: application/json")
    @POST("messages")
    Call<Message> postMessage(@Body Message message, @Header("Authorization") String Token);
//    @GET("messages/users")
//    Call<Event> getUsersMessaged(); //WHICH ARE THE PARAMETERS??//
    @GET("messages/{USER_ID}")
    Call<List<Message>> getUserIDMessages(@Path("USER_ID") int user_id, @Header("Authorization") String Token);

}
