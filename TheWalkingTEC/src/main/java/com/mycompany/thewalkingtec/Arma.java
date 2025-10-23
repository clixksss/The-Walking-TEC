/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.thewalkingtec;

import java.awt.Color;

/**
 *
 * @author gabos
 */
import java.awt.Color;
import javax.swing.JButton;

public class Arma extends Army implements Runnable {
    private int daño = 10;
    private int rango = 3;  // en celdas
    private int fila;
    private int columna;
    private Army[][] mapa;
    private JButton[][] celdas;
    private boolean activa = true;
    private int tam;
    
    public Arma(int fila, int columna, Army[][] mapa, JButton[][] celdas, int tam) {
        super(15, 5, new Color(220, 200, 60), 'A');
        this.fila = fila;
        this.columna = columna;
        this.mapa = mapa;
        this.celdas = celdas;
        this.tam = tam;
    }
    public Arma() {
        super(15, 5, new Color(220, 200, 60), 'A');
    }
    

    @Override
    public void atacar(Army objetivo) {
        if (objetivo != null && objetivo instanceof Zombie) {
            objetivo.recibirGolpe(daño);
        }
    }

    @Override
    public void run() {
        try {
            while (activa && this.getVida() > 0) {
                buscarYAtacarZombies();
                Thread.sleep(1000); // velocidad de disparo (1 seg)
            }
        } catch (InterruptedException e) {
            System.out.println("Arma interrumpida");
        }
    }

    private void buscarYAtacarZombies() {
        for (int f = Math.max(0, fila - rango); f <= Math.min(tam - 1, fila + rango); f++) {
            for (int c = Math.max(0, columna - rango); c <= Math.min(tam - 1, columna + rango); c++) {
                Army posible = mapa[f][c];
                if (posible instanceof Zombie) {
                    atacar(posible);

                    // Si muere, limpiar visualmente
                    if (!posible.estaVivo()) {
                        mapa[f][c] = null;
                        celdas[f][c].setText("");
                        celdas[f][c].setBackground(Color.WHITE);
                        celdas[f][c].repaint();
                    }
                    return; // ataca solo a un zombie por turno
                }
            }
        }
    }

    public void detener() {
        activa = false;
    }
}
