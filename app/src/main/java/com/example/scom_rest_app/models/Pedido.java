package com.example.scom_rest_app.models;

import java.util.Date;

public class Pedido {
    private int ciCamarero;
    private int ciChef;
    private int codFactura;
    private String estado;
    private String fecha;
    private int idpedido;

    public Pedido(int ciCamarero, int ciChef, int codFactura, String estado, String fecha, int idpedido) {
        this.ciCamarero = ciCamarero;
        this.ciChef = ciChef;
        this.codFactura = codFactura;
        this.estado = estado;
        this.fecha = fecha;
        this.idpedido = idpedido;
    }

    public int getCiCamarero() {
        return ciCamarero;
    }

    public void setCiCamarero(int ciCamarero) {
        this.ciCamarero = ciCamarero;
    }

    public int getCiChef() {
        return ciChef;
    }

    public void setCiChef(int ciChef) {
        this.ciChef = ciChef;
    }

    public int getCodFactura() {
        return codFactura;
    }

    public void setCodFactura(int codFactura) {
        this.codFactura = codFactura;
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

    public int getIdpedido() {
        return idpedido;
    }

    public void setIdpedido(int idpedido) {
        this.idpedido = idpedido;
    }
}
