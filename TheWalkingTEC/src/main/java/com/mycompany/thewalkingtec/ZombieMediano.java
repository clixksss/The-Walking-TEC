/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.thewalkingtec;

import java.awt.Color;
import javax.swing.JButton;

/**
 *
 * @author gabos
 */
public class ZombieMediano extends Zombie {
    private static int nivelAparicion = 3;
    private int alcance = 3;

    public ZombieMediano(int f, int c, Army[][] m, JButton[][] celdas, int tam) {
        super(90, 5, new Color(255, 220, 60), 'M', f, c, m, celdas, tam, 700, 5);
    }
    

    public ZombieMediano(int vida, int dano, int nivelApa, int f, int c, Army[][] m, JButton[][] celdas, int tam) {
        super(vida, dano, new Color(200, 80, 0), 'M', f, c, m, celdas, tam, 450, nivelApa);
    }
    public static int getNivelAparicion() {
        return nivelAparicion;
    }

    @Override
    public void atacar(Army objetivo) {
        if (objetivo instanceof Defensa || objetivo instanceof Arma && !(objetivo instanceof ArmaAerea)) {
            objetivo.recibirGolpe(daño, this);
            registrarAtaque(objetivo, daño);
        }
    }

    @Override
    protected void moverYAtacar() {
        int centro = tam / 2;

        Army objetivo = buscarObjetivoEnRango();

        if (objetivo != null) {
            atacar(objetivo);
            parpadearAtaque();
        } else {
            int df = Integer.compare(centro, fila);
            int dc = Integer.compare(centro, columna);
            moverSinChocar(df, dc);
        }
    }

    private Army buscarObjetivoEnRango() {
        for (int f = Math.max(0, fila - alcance); f <= Math.min(tam - 1, fila + alcance); f++) {
            for (int c = Math.max(0, columna - alcance); c <= Math.min(tam - 1, columna + alcance); c++) {
                Army obj = mapa[f][c];
                if (obj instanceof Defensa || obj instanceof Arma) {
                    return obj;
                }
            }
        }
        return null;
    }

    private void parpadearAtaque() {
        celdas[fila][columna].setBackground(new Color(255, 255, 180));
        try { Thread.sleep(100); } catch (InterruptedException ignored) {}
        celdas[fila][columna].setBackground(getColor());
    }

    private void moverSinChocar(int df, int dc) {
        int nuevaFila = fila + df;
        int nuevaCol = columna + dc;

        if (nuevaFila < 0 || nuevaFila >= tam || nuevaCol < 0 || nuevaCol >= tam) return;

        synchronized (mapa) {
            Army destino = mapa[nuevaFila][nuevaCol];
            if (destino == null || destino instanceof Defensa || destino instanceof Arma) {
                moverA(nuevaFila, nuevaCol);
            }
        }
    }

    @Override
    public int getFila() { return fila; }
    @Override
    public int getColumna() { return columna; }
}
