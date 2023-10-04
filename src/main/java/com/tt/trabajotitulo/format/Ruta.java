package com.tt.trabajotitulo.format;

import com.tt.trabajotitulo.PSO_DARP.Problem;

import java.util.List;

public class Ruta {
    private int nodo;
    private String pasajero;
    private String accion;

    public Ruta() {

    }

    public Ruta(Integer nodo) {
        this.nodo = nodo;

        if (nodo == 0) {
            this.pasajero = "Origen";
        } else if (nodo <= Problem.n[Problem.type]) {
            this.pasajero = "Pasajero " + (nodo);
        } else {
            this.pasajero = "Pasajero " + (nodo - Problem.n[Problem.type]);
        }

        if (nodo == 0) {
            this.accion = "Origen";
        } else if (nodo <= Problem.n[Problem.type]) {
            this.accion = "Sube";
        } else {
            this.accion = "Baja";
        }
    }

    public Ruta(int nodo, String s) {
        this.nodo = nodo;
        this.pasajero = s;
        this.accion = s;
    }

    public int getNodo() {
        return nodo;
    }

    public void setNodo(int nodo) {
        this.nodo = nodo;
    }

    public String getPasajero() {
        return pasajero;
    }

    public void setPasajero(String pasajero) {
        this.pasajero = pasajero;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }
}
