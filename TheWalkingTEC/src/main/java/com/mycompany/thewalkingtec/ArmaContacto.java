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
public class ArmaContacto extends Arma {
    public ArmaContacto(int f, int c, Army[][] m, JButton[][] celdas, int tam) {
        super(20, 10, new Color(60, 200, 60), 'C', f, c, m, celdas, tam, 1);
    }
    public ArmaContacto(int vida,int dano,int rango,int f, int c, Army[][] m, JButton[][] celdas, int tam) {
        super(vida, dano, new Color(255, 128, 0), 'C', f, c, m, celdas, tam, rango);
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
        for (int df = -1; df <= 1; df++) {
            for (int dc = -1; dc <= 1; dc++) {
                int nf = fila + df, nc = columna + dc;
                if (nf >= 0 && nc >= 0 && nf < tam && nc < tam) {
                    Army posible = mapa[nf][nc];
                    if (posible instanceof Zombie) {
                        atacar(posible);
                        if (!posible.estaVivo()) {
                            mapa[nf][nc] = null;
                            celdas[nf][nc].setText("");
                            celdas[nf][nc].setBackground(Color.WHITE);
                        }
                        return;
                    }
                }
            }
        }
    }

    @Override
    public int getFila() { return fila; }
    @Override
    public int getColumna() { return columna; }
}

