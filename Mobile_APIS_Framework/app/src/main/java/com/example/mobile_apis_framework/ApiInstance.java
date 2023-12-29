package com.example.mobile_apis_framework;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiInstance {
// use the local ip of your system
    private static final String BASE_URL = "http://192.168.2.176:3000";
    private static ApiInstance instance;

    private final Retrofit retrofit;

    private ApiInstance() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized ApiInstance getInstance() {
        if (instance == null) {
            instance = new ApiInstance();
        }
        return instance;
    }

    public ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }
}
