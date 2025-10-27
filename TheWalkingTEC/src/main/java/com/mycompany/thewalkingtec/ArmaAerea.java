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
public class ArmaAerea extends Arma {
    public ArmaAerea(int f, int c, Army[][] m, JButton[][] celdas, int tam) {
        super(10, 12, new Color(120, 180, 255), 'A', f, c, m, celdas, tam, 10);
    }
    public ArmaAerea(int vida,int dano,int rango,int f, int c, Army[][] m, JButton[][] celdas, int tam) {
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
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                Army posible = mapa[i][j];
                if (posible instanceof Zombie) {
                    atacar(posible);
                    celdas[i][j].setBackground(Color.RED);
                    try { Thread.sleep(80); } catch (InterruptedException ignored) {}
                    celdas[i][j].setBackground(Color.WHITE);
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
