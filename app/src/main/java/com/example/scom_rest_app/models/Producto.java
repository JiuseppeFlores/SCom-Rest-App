package com.example.scom_rest_app.models;

public class Producto {
    private int idProducto;
    private String estado;
    private String nombre;
    private double precio;
    private String tipoProducto;

    public Producto(){

    }
    public Producto(int id, String estado, String nombre, double precio, String tipo){
        this.idProducto = id;
        this.estado = estado;
        this.nombre = nombre;
        this.precio = precio;
        this.tipoProducto = tipo;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }
}
