package com.karmadev.drivecat.model;

public class Actividad {
    private String nombre;
    private String ruta;
    private String cedula;

    public Actividad(String nombre, String ruta, String cedula) {
        this.nombre = nombre;
        this.ruta = ruta;
        this.cedula = cedula;
    }

    public String getNombre() {

        return nombre;
    }

    public String getRuta() {

        return ruta;
    }

    public String getCedula() {
        return cedula;
    }
}
