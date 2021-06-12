package com.lasalle.meet.apiadapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lasalle.meet.enities.Event;
import com.lasalle.meet.enities.User;

import kotlin.sequences.USequencesKt;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
    @Headers("Content-Type: multipart/form-data")
    @POST("users")
    public Call<User> postCreateUser(@Body User user);

//    @GET("users")
//    Call<User> getUser();
//    @GET("users/")
//    Call<User> getUserID();
//    @GET("users/search/?s=john")
//    Call<User> searchUser(@Field("s") String s);
//    @PUT("users")
//    Call<User> modifyUser(@Body User user);
//    @DELETE("users")
//    Call<User> deleteUser();
//    @GET("users/ID/friends")
//    Call<Event> getUserFriend(@Field("ID") int ID);
//
//    //EVENTS//
//    @GET("users/ID/events")
//    Call<Event> getUserEvent(@Field("ID") int ID);
//    @GET("users/ID/events/future")
//    Call<Event> getFutureUserEvent(@Field("ID") int ID);
//    @GET("users/ID/events/finished")
//    Call<Event> getFinishedUserEvent(@Field("ID") int ID);
//    @GET("users/ID/events/current")
//    Call<Event> getCurrentUserEvent(@Field("ID") int ID);
//    @GET("users/ID/assistances")
//    Call<Event> getUserAssistances(@Field("ID") int ID);
//    @GET("users/ID/assistances/future")
//    Call<Event> getFutureUserAssistances(@Field("ID") int ID);
//    @GET("users/ID/assistances/finished")
//    Call<Event> getFinishedUserAssistances(@Field("ID") int ID);
//
//    //EVENT CRUD//
//    @POST("events")
//    Call<Event> postEvent(@Body Event event);
//    @GET("events")
//    Call<Event> getEvent(@Field("t") String type);
//    @GET("events/ID")
//    Call<Event> getEventID();
//    @PUT("events/ID")
//    Call<Event> modifyEvent(@Body Event event);
//    @DELETE("events/ID")
//    Call<Event> deleteEvent(); //WHICH ARE THE PARAMETERS??//
//
//    //EVENT ASSISTENCES//
//    @GET("events/ID/assistances")
//    Call<Event> getEventAssistans(@Field("t") String type); //WHICH ARE THE PARAMETERS??//
//    @GET("events/ID/assistances/ID_USER")
//    Call<Event> getUserIDAssistan(@Field("t") String type); //WHICH ARE THE PARAMETERS??//
//    @POST("events/ID/assistances")
//    Call<Event> postUserAssistanInfo(@Field("ID") int eventID, @Field("puntuation") int puntuation, @Field("comentary") String comentary); //WHICH ARE THE PARAMETERS??//
//    @PUT("events/ID/assistances")
//    Call<Event> modifyUserAssistanInfo(@Field("ID") int eventID, @Field("puntuation") int puntuation, @Field("comentary") String comentary); //WHICH ARE THE PARAMETERS??//
//    @DELETE("events/ID/assistances")
//    Call<Event> deleteAssistance(@Field("ID") int eventID); //WHICH ARE THE PARAMETERS??//
//
//    //FRIENDS//
//    @GET("friends/requests")
//    Call<Event> getFriendRequests(); //WHICH ARE THE PARAMETERS??//
//    @GET("friends")
//    Call<Event> getFriends(); //WHICH ARE THE PARAMETERS??//
//    @POST("friends/ID")
//    Call<Event> postFriendRequest(); //WHICH ARE THE PARAMETERS??//
//    @PUT("friends/ID")
//    Call<Event> putFriendRequestAccept(); //WHICH ARE THE PARAMETERS??//
//    @DELETE("friends/ID")
//    Call<Event> deleteFriendRequest(@Field("ID") int ID); //WHICH ARE THE PARAMETERS??//
//
//    //MESSAGES//
//    @POST("messages")
//    Call<Event> postMessage(@Field("content") String content, @Field("user_id_send") int user_id_send, @Field("user_id_recived") int user_id_recived); //WHICH ARE THE PARAMETERS??//
//    @GET("messages/users")
//    Call<Event> getUsersMessaged(); //WHICH ARE THE PARAMETERS??//
//    @GET("messages/USER_ID")
//    Call<Event> getUserIDMessages(@Field("USER_ID") int user_id); //WHICH ARE THE PARAMETERS??//

}
