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
public class ArmaMultiple extends Arma {
    private int disparos = 3;

    public ArmaMultiple(int f, int c, Army[][] m, JButton[][] celdas, int tam) {
        super(20, 6, new Color(170, 100, 255), 'X', f, c, m, celdas, tam, 4);
    }
    public ArmaMultiple(int vida,int dano,int rango,int f, int c, Army[][] m, JButton[][] celdas, int tam) {
        super(vida, dano, new Color(161, 130, 98), 'X', f, c, m, celdas, tam, rango);
    }

    @Override
    public void atacar(Army objetivo) {
        if (!(objetivo instanceof Zombie z ) || objetivo instanceof ZombieAereo) return;

        z.recibirGolpe(daño, this);
        registrarAtaque(z, daño);

        if (!z.estaVivo()) {
            z.detener();
            mapa[z.fila][z.columna] = null;
            JButton b = celdas[z.fila][z.columna];
            b.setText("");
            b.setBackground(Color.WHITE);
        }
    }


    @Override
    protected void buscarYAtacar() {
        int realizados = 0;
        for (int f = Math.max(0, fila - rango); f <= Math.min(tam - 1, fila + rango); f++) {
            for (int c = Math.max(0, columna - rango); c <= Math.min(tam - 1, columna + rango); c++) {
                Army posible = mapa[f][c];
                if (posible instanceof Zombie && !(posible instanceof ZombieAereo)) {
                    atacar(posible);
                    realizados++;
                    if (realizados >= disparos) return;
                }
            }
        }
    }
    @Override
    public int getFila() { return fila; }
    @Override
    public int getColumna() { return columna; }
}

