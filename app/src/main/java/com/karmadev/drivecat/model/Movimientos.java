package com.karmadev.drivecat.model;

public class Movimientos {
    private long id;
    private String usuario;
    private String ruta;
    private String transporte;
    private String placa;
    private String fecha;


    public Movimientos(long id, String usuario, String ruta, String transporte, String placa, String fecha) {
        this.id = id;
        this.usuario = usuario;
        this.ruta = ruta;
        this.transporte = transporte;
        this.placa = placa;
        this.fecha = fecha;
    }

    public long getId() {
        return id;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getRuta() {
        return ruta;
    }

    public String getTransporte() {
        return transporte;
    }

    public String getPlaca() {
        return placa;
    }

    public String getFecha() {
        return fecha;
    }
}
