package com.lasalle.meet.apiadapter;

import com.lasalle.meet.enities.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface JsonPlaceHolderAPI {

    @POST("users/login")
    public Call<User> createUser(@Body User user);

}
