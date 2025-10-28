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
public class ArmaImpacto extends Arma {
    private int radio = 1; 

    public ArmaImpacto(int f, int c, Army[][] m, JButton[][] celdas, int tam) {
        super(10, 15, new Color(255, 140, 60), 'I', f, c, m, celdas, tam, 0);
    }

    public ArmaImpacto(int vida, int dano, int rango, int f, int c, Army[][] m, JButton[][] celdas, int tam) {
        super(vida, dano, new Color(255, 140, 60), 'I', f, c, m, celdas, tam, rango);
    }

    @Override
    public void atacar(Army z) {
    }

    @Override
    protected void buscarYAtacar() {
        while (activa) {
            if (detectarZombieCercano()) {
                explotar();
                break;
            }

            try {
                Thread.sleep(200); 
            } catch (InterruptedException ignored) {}
        }
    }

    private boolean detectarZombieCercano() {
        for (int df = -radio; df <= radio; df++) {
            for (int dc = -radio; dc <= radio; dc++) {
                if (df == 0 && dc == 0) continue; 

                int nf = fila + df;
                int nc = columna + dc;
                if (nf < 0 || nf >= tam || nc < 0 || nc >= tam) continue;

                Army posible = mapa[nf][nc];
                if (posible instanceof Zombie) {
                    return true;
                }
            }
        }
        return false;
    }

    private void explotar() {
        int dañoExplosion = this.daño * 2;

        for (int f = Math.max(0, fila - radio); f <= Math.min(tam - 1, fila + radio); f++) {
            for (int c = Math.max(0, columna - radio); c <= Math.min(tam - 1, columna + radio); c++) {
                Army posible = mapa[f][c];
                if (posible instanceof Zombie z) {
                    z.recibirGolpe(dañoExplosion, this);
                    registrarAtaque(z, dañoExplosion);
                }
                celdas[f][c].setBackground(new Color(255, 100, 0));
            }
        }

        celdas[fila][columna].setText("💥");

        try { Thread.sleep(200); } catch (InterruptedException ignored) {}

        for (int f = Math.max(0, fila - radio); f <= Math.min(tam - 1, fila + radio); f++) {
            for (int c = Math.max(0, columna - radio); c <= Math.min(tam - 1, columna + radio); c++) {
                if (!(mapa[f][c] instanceof Zombie)) {
                    celdas[f][c].setBackground(Color.WHITE);
                    celdas[f][c].setText("");
                }
            }
        }

        mapa[fila][columna] = null;
        activa = false;
    }

    @Override
    public int getFila() { return fila; }

    @Override
    public int getColumna() { return columna; }
}




