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

public abstract class Arma extends Army implements Runnable {
    protected int daño;
    protected int rango;
    protected int fila;
    protected int columna;
    protected Army[][] mapa;
    protected JButton[][] celdas;
    protected boolean activa = true;
    protected int tam;
    protected static int dañoBase = 5;

    public static void setDañoBase(int d) { dañoBase = d; }

    public Arma(int vida, int daño, Color color, char simbolo, 
                int fila, int columna, Army[][] mapa, JButton[][] celdas, int tam, int rango) {
        super(vida, daño, color, simbolo);
        this.daño = daño;
        this.rango = rango;
        this.fila = fila;
        this.columna = columna;
        this.mapa = mapa;
        this.celdas = celdas;
        this.tam = tam;
    }

    @Override
    public abstract void atacar(Army objetivo);

    @Override
    public void run() {
        try {
            while (activa && this.getVida() > 0) {
                for (int f = Math.max(0, fila - rango); f <= Math.min(tam - 1, fila + rango); f++) {
                    for (int c = Math.max(0, columna - rango); c <= Math.min(tam - 1, columna + rango); c++) {
                        Army posible = mapa[f][c];

                        if (posible instanceof Zombie z) {
                            atacar(z);

                            if (!z.estaVivo()) {
                                z.detener(); 
                                mapa[f][c] = null; 
                                celdas[f][c].setText("");
                                celdas[f][c].setBackground(Color.WHITE);
                                celdas[f][c].repaint();
                                celdas[f][c].revalidate();

                                System.out.println("💀 Zombie eliminado en (" + f + "," + c + ")");
                            }

                            break;
                        }
                    }
                }

                Thread.sleep(1000); 
            }
        } catch (InterruptedException e) {
            System.out.println("⚠️ Arma interrumpida");
        }
    }

    protected abstract void buscarYAtacar();

    public void detener() { activa = false; }
}
