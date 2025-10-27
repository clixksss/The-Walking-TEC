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
public class ZombieContacto extends Zombie {

    public ZombieContacto(int f, int c, Army[][] m, JButton[][] celdas, int tam) {
        super(vidaBase, dañoBase, new Color(220, 60, 60), 'Z', f, c, m, celdas, tam, 500, 1);
    }
    public ZombieContacto(int vida,int dano,int nivelApa,int f, int c, Army[][] m, JButton[][] celdas, int tam) {
        super(vida, dano, new Color(120, 180, 255), '.', f, c, m, celdas, tam, 450, nivelApa);
    }


    @Override
    public void atacar(Army objetivo) {
        if (objetivo instanceof Reliquia || objetivo instanceof Defensa) {
            objetivo.recibirGolpe(daño, this);
            registrarAtaque(objetivo, daño);
        }
    }



    @Override
    protected void moverYAtacar() {
        int centro = tam / 2;
        int deltaFila = Integer.compare(centro, fila);
        int deltaCol = Integer.compare(centro, columna);
        int nf = fila + deltaFila;
        int nc = columna + deltaCol;

        if (nf < 0 || nc < 0 || nf >= tam || nc >= tam) return;
        Army objetivo = mapa[nf][nc];

        if (objetivo == null) {
            moverA(nf, nc);
        } else if (objetivo instanceof Reliquia || objetivo instanceof Defensa || objetivo instanceof Arma) {
            atacar(objetivo);
            if (!objetivo.estaVivo()) {
                mapa[nf][nc] = null;
                celdas[nf][nc].setBackground(Color.WHITE);
                celdas[nf][nc].setText("");
            }
        }
    }
    @Override
    public int getFila() { return fila; }
    @Override
    public int getColumna() { return columna; }
}

