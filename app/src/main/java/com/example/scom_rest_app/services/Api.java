package com.example.scom_rest_app.services;

import retrofit.RestAdapter;

public class Api {

    public static final String BASE_URL = "https://scom-rest.herokuapp.com/api";

    public static ServiceApi getClient() {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .build();

        ServiceApi api = adapter.create(ServiceApi.class);
        return api;
    }
}
