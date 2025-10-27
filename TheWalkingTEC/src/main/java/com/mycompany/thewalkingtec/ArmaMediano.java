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
public class ArmaMediano extends Arma {
    public ArmaMediano(int f, int c, Army[][] m, JButton[][] celdas, int tam) {
        super(15, 8, new Color(240, 220, 70), 'M', f, c, m, celdas, tam, 3);
    }
    public ArmaMediano(int vida,int dano,int rango,int f, int c, Army[][] m, JButton[][] celdas, int tam) {
        super(vida, dano, new Color(120, 180, 255), 'A', f, c, m, celdas, tam, rango);
    }

    @Override
    public void atacar(Army objetivo) {
        if (!(objetivo instanceof Zombie z)) return;

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
        for (int f = Math.max(0, fila - rango); f <= Math.min(tam - 1, fila + rango); f++) {
            for (int c = Math.max(0, columna - rango); c <= Math.min(tam - 1, columna + rango); c++) {
                Army posible = mapa[f][c];
                if (posible instanceof Zombie) {
                    atacar(posible);
                    celdas[fila][columna].setBackground(new Color(255, 255, 180));
                    try { Thread.sleep(100); } catch (InterruptedException ignored) {}
                    celdas[fila][columna].setBackground(getColor());
                    return;
                }
            }
        }
    }
    @Override
    public int getFila() { return fila; }
    @Override
    public int getColumna() { return columna; }
}

