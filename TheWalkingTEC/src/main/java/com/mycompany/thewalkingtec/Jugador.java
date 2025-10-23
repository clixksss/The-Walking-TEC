/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.thewalkingtec;

/**
 *
 * @author gabos
 */

public class Jugador {
    private String nombre;
    private int nivelActual;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.nivelActual = 1;
    }

    public String getNombre() { return nombre; }
    public int getNivelActual() { return nivelActual; }
    public void subirNivel() { nivelActual++; }

    public void setNivelActual(int nivel) { this.nivelActual = nivel; }
}

