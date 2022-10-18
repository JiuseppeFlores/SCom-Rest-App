package com.example.scom_rest_app.models;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

public class Pedido {
    private int idPedido;
    private String estado;
    private String fecha;
    private int idMesa;
    private int ciCamarero;
    private int codFactura;
    private int ciChef;
    private Array productos;

    public Pedido(int idPedido, String estado, String fecha, int mesa) {
        this.idPedido = idPedido;
        this.estado = estado;
        this.fecha = fecha;
        this.idMesa = mesa;
    }

    public Pedido(String estado, String fecha, int mesa) {
        this.estado = estado;
        this.fecha = fecha;
        this.idMesa = mesa;
    }

    public int getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(int idMesa) {
        this.idMesa = idMesa;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idpedido) {
        this.idPedido = idpedido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getCiCamarero() {
        return ciCamarero;
    }

    public void setCiCamarero(int ciCamarero) {
        this.ciCamarero = ciCamarero;
    }

    public int getCodFactura() {
        return codFactura;
    }

    public void setCodFactura(int codFactura) {
        this.codFactura = codFactura;
    }

    public int getCiChef() {
        return ciChef;
    }

    public void setCiChef(int ciChef) {
        this.ciChef = ciChef;
    }
}
