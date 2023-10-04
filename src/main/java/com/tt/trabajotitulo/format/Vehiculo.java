package com.tt.trabajotitulo.format;

import java.util.ArrayList;
import java.util.List;

public class Vehiculo {
    private int id;
    private String nombre;
    private List<Ruta> rutas;

    public Vehiculo(int id, ArrayList<Ruta> rutas) {
        this.id = id;
        this.nombre = "Vehiculo " + (id + 1);
        this.rutas = rutas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Ruta> getRutas() {
        return rutas;
    }

    public void setRutas(List<Ruta> rutas) {
        this.rutas = rutas;
    }
}
