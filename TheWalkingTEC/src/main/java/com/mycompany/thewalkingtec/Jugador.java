/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.thewalkingtec;

import java.io.Serializable;

/**
 *
 * @author gabos
 */

public class Jugador implements Serializable {
    private String nombre;
    private int nivelActual = 1;

    public Jugador(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() { return nombre; }
    public int getNivelActual() { return nivelActual; }
    public void subirNivel() { nivelActual++; }
    public int getCapacidadDefensas() {
    return 20 + (nivelActual - 1) * 5;
    }

    public void setNivelActual(int nivel) { this.nivelActual = nivel; }
}

