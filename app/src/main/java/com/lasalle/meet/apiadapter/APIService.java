package com.lasalle.meet.apiadapter;

import com.lasalle.meet.enities.User;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers("Content-Type: multipart/form-data")
    @POST("users")
    public Call<User> postCreateUser(@Body User user);
}
