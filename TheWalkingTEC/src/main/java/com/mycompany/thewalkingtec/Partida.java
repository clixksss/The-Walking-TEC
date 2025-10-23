/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.thewalkingtec;

/**
 *
 * @author gabos
 */
import java.io.Serializable;

public class Partida implements Serializable {
    private Jugador jugador;
    private Army[][] mapa;
    private int nivel;

    public Partida(Jugador jugador, Army[][] mapa, int nivel) {
        this.jugador = jugador;
        this.mapa = mapa;
        this.nivel = nivel;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public Army[][] getMapa() {
        return mapa;
    }

    public int getNivel() {
        return nivel;
    }
}
