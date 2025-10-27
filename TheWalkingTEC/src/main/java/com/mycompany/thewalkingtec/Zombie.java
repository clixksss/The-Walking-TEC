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

public abstract class Zombie extends Army implements Runnable {

    protected int fila;
    protected int columna;
    protected Army[][] mapa;
    protected JButton[][] celdas;
    protected boolean activo = true;
    protected int tam;
    protected int daño;
    protected int velocidad; 
    protected int nivelAparicion; 

    protected static int vidaBase = 30;
    protected static int dañoBase = 5;

    public Zombie(int vida, int daño, Color color, char simbolo,int fila, int columna, Army[][] mapa, JButton[][] celdas, int tam, int velocidad, int nivelAparicion) {

        super(vida, daño, color, simbolo);
        this.fila = fila;
        this.columna = columna;
        this.mapa = mapa;
        this.celdas = celdas;
        this.tam = tam;
        this.daño = daño;
        this.velocidad = velocidad;
        this.nivelAparicion = nivelAparicion;
    }


    @Override
    public abstract void atacar(Army objetivo);

    @Override
    public void run() {
        try {
            while (activo && this.getVida() > 0) {
                moverYAtacar();
                Thread.sleep(velocidad);
            }

            if (this.getVida() <= 0) {
                activo = false;

                mapa[fila][columna] = null;
                if (celdas != null && fila >= 0 && columna >= 0 && 
                    fila < celdas.length && columna < celdas[0].length) {
                    JButton b = celdas[fila][columna];
                    b.setBackground(Color.WHITE);
                    b.setText("");
                    b.repaint();
                    b.revalidate();
                }

                System.out.println("💀 Zombie eliminado en (" + fila + "," + columna + ")");
            }

        } catch (InterruptedException e) {
            System.out.println("Zombie interrumpido");
        }
    }


    protected abstract void moverYAtacar();

    protected void moverA(int nuevaFila, int nuevaCol) {
        if (nuevaFila < 0 || nuevaFila >= tam || nuevaCol < 0 || nuevaCol >= tam) return;

        Army destino = mapa[nuevaFila][nuevaCol];
        if (destino instanceof Zombie && destino != this) {
            return; // ❌ Evita colisión
        }

        mapa[fila][columna] = null;
        celdas[fila][columna].setBackground(Color.WHITE);
        celdas[fila][columna].setText("");

        fila = nuevaFila;
        columna = nuevaCol;
        mapa[fila][columna] = this;

        celdas[fila][columna].setBackground(getColor());
        celdas[fila][columna].setText(String.valueOf(getSimbolo()));
    }



    public static void setVidaBase(int v) { vidaBase = v; }

    public static void setDañoBase(int d) { dañoBase = d; }

    public void detener() { activo = false; }
}

