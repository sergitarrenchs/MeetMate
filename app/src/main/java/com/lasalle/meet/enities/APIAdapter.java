package com.lasalle.meet.enities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lasalle.meet.apiadapter.APIService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIAdapter{

    private static APIService apiService;

    private static Retrofit retrofit;

    public static APIService getApiService() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        Gson gson =  new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        if (apiService == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://puigmal.salle.url.edu/api/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();
            apiService = retrofit.create(APIService.class);
        }

        return apiService;
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }
}
