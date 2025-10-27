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
public class ZombieChoque extends Zombie {
    private static int nivelAparicion = 7;

    private int radioExplosion = 2;

    public ZombieChoque(int f, int c, Army[][] m, JButton[][] celdas, int tam) {
        super(70, 20, new Color(255, 140, 0), 'C', f, c, m, celdas, tam, 400, 7);
    }
    public ZombieChoque(int vida,int dano,int nivelApa,int f, int c, Army[][] m, JButton[][] celdas, int tam) {
        super(vida, dano, new Color(100, 0, 120), 's', f, c, m, celdas, tam, 450, nivelApa);
    }
    public static int getNivelAparicion() {
        return nivelAparicion;
    }

    @Override
    public void atacar(Army objetivo) {
    }


    @Override
    protected void moverYAtacar() {
        int centro = tam / 2;
        int df = Integer.compare(centro, fila);
        int dc = Integer.compare(centro, columna);
        int nf = fila + df;
        int nc = columna + dc;

        if (nf < 0 || nc < 0 || nf >= tam || nc >= tam) return;
        Army objetivo = mapa[nf][nc];

        if (objetivo == null) {
            moverA(nf, nc);
        } else if (objetivo instanceof Reliquia || objetivo instanceof Defensa || objetivo instanceof Arma && !(objetivo instanceof ArmaAerea)) {
            explotar();
        }
    }

    private void explotar() {
        for (int f = Math.max(0, fila - radioExplosion); f <= Math.min(tam - 1, fila + radioExplosion); f++) {
            for (int c = Math.max(0, columna - radioExplosion); c <= Math.min(tam - 1, columna + radioExplosion); c++) {
                Army objetivo = mapa[f][c];
                if (objetivo instanceof Defensa || objetivo instanceof Reliquia) {
                    objetivo.recibirGolpe(daño);
                    if (!objetivo.estaVivo()) {
                        mapa[f][c] = null;
                        celdas[f][c].setBackground(Color.WHITE);
                        celdas[f][c].setText("");
                    }
                }
            }
        }

        celdas[fila][columna].setBackground(Color.RED);
        celdas[fila][columna].setText("💥");
        try { Thread.sleep(150); } catch (InterruptedException ignored) {}
        mapa[fila][columna] = null;
        celdas[fila][columna].setBackground(Color.WHITE);
        celdas[fila][columna].setText("");
        activo = false;
    }
    @Override
    public int getFila() { return fila; }
    @Override
    public int getColumna() { return columna; }
}
