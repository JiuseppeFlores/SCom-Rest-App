package com.example.scom_rest_app.services;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

public interface ServiceApi {
    @FormUrlEncoded
    @POST("/login")
    public void iniciarSesion(
            @Field("user") String user,
            @Field("password") String password,
            Callback<Response> response
    );

    @GET("/productos?estado=habilitado")
    public void obtenerProductosHabilitados(
            Callback<Response> response
    );

    @GET("/pedidos")
    public void obtenerPedidos(
            Callback<Response> response
    );

    @FormUrlEncoded
    @POST("/cliente")
    public void crearCuenta(
            @Field("ci") String ci,
            @Field("user") String user,
            @Field("password") String password,
            @Field("email") String email,
            @Field("name") String name,
            @Field("apellidoPat") String apellidoPat,
            @Field("apellidoMat") String apellidoMat,
            @Field("fechaNaci") String fechaNaci,
            @Field("nit") String nit,
            @Field("estado") String estado,
            Callback<Response> response
    );

    @FormUrlEncoded
    @POST("/crearpedido")
    public void realizarPedido(
            @Field("estado") String estado,
            @Field("fecha") String fecha,
            @Field("idMesa") int idMesa,
            @Field("productos") String productos,
            Callback<Response> response
    );

    @FormUrlEncoded
    @POST("/obtenerproducto")
    public void obtenerProducto(
            @Field("idProducto") int idProducto,
            Callback<Response> response
    );

    @GET("/mesas")
    public void obtenerMesas(
            Callback<Response> response
    );

    @FormUrlEncoded
    @POST("/obtenerPedido")
    public void obtenerPedido(
            @Field("idPedido") int idPedido,
            Callback<Response> response
    );

    @FormUrlEncoded
    @POST("/confirmarPedido")
    public void confirmarPedido(
            @Field("idPedido") int idPedido,
            @Field("ciCamarero") int ciCamarero,
            Callback<Response> response
    );

    @FormUrlEncoded
    @POST("/cancelarPedido")
    public void cancelarPedido(
            @Field("idPedido") int idPedido,
            @Field("ciCamarero") int ciCamarero,
            Callback<Response> response
    );

    @FormUrlEncoded
    @POST("/entregarPedido")
    public void entregarPedido(
            @Field("idPedido") int idPedido,
            @Field("ciCamarero") int ciCamarero,
            Callback<Response> response
    );
}
