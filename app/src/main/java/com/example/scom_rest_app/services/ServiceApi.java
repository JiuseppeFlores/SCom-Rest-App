package com.example.scom_rest_app.services;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface ServiceApi {
    @FormUrlEncoded
    @POST("/login")
    public void iniciarSesion(
            @Field("user") String user,
            @Field("password") String password,
            Callback<Response> response
    );
}
