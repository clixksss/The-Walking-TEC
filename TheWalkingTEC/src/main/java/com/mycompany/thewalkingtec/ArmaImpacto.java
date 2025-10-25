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
    private int radio = 2;

    public ArmaImpacto(int f, int c, Army[][] m, JButton[][] celdas, int tam) {
        super(10, 15, new Color(255, 140, 60), 'I', f, c, m, celdas, tam, 0);
    }

    @Override
    public void atacar(Army z) {}

    @Override
    protected void buscarYAtacar() {
        Army obj = mapa[fila][columna];
        if (obj instanceof Zombie) explotar();
    }

    private void explotar() {
        for (int f = Math.max(0, fila - radio); f <= Math.min(tam - 1, fila + radio); f++) {
            for (int c = Math.max(0, columna - radio); c <= Math.min(tam - 1, columna + radio); c++) {
                Army posible = mapa[f][c];
                if (posible instanceof Zombie) posible.recibirGolpe(daño);
            }
        }
        celdas[fila][columna].setBackground(Color.RED);
        celdas[fila][columna].setText("💥");
        try { Thread.sleep(200); } catch (InterruptedException ignored) {}
        mapa[fila][columna] = null;
        celdas[fila][columna].setBackground(Color.WHITE);
        celdas[fila][columna].setText("");
        activa = false;
    }
    @Override
    public int getFila() { return fila; }
    @Override
    public int getColumna() { return columna; }
}

