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
public class ZombieAereo extends Zombie {

    public ZombieAereo(int f, int c, Army[][] m, JButton[][] celdas, int tam) {
        super(80, 6, new Color(120, 180, 255), 'A', f, c, m, celdas, tam, 450, 3);
    }
    public ZombieAereo(int vida,int dano,int nivelApa,int f, int c, Army[][] m, JButton[][] celdas, int tam) {
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
        } else if (objetivo instanceof Reliquia) {
            atacar(objetivo);
        }
    }
    @Override
    public int getFila() { return fila; }
    @Override
    public int getColumna() { return columna; }
}
