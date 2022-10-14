package com.example.scom_rest_app.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

public class Pedido {
    private int idpedido;
    private String estado;
    private String fecha;
    private int ciCamarero;
    private int codFactura;
    private int ciChef;
    private String hora;

    public Pedido(String estado, String fecha, int ciCamarero, int codFactura, int ciChef) {
        this.estado = estado;
        this.fecha = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        this.hora = "0:00";
        this.ciCamarero = ciCamarero;
        this.ciChef = ciChef;
        this.codFactura = codFactura;

        //Date datoFecha = new Date();
        //this.fecha = datoFecha.getTime();

    }

    public int getIdpedido() {
        return idpedido;
    }

    public void setIdpedido(int idpedido) {
        this.idpedido = idpedido;
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

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
