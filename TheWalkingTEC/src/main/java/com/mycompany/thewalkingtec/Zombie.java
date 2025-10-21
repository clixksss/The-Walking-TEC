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

public class Zombie extends Army implements Runnable {
    private int daño = 3;
    private int fila;
    private int columna;
    private Army[][] mapa;
    private JButton[][] celdas;
    private boolean activo = true;
    private int tam;

    public Zombie(int fila, int columna, Army[][] mapa, JButton[][] celdas, int tam) {
        super(10, 10, new Color(220, 60, 60), 'Z');
        this.fila = fila;
        this.columna = columna;
        this.mapa = mapa;
        this.celdas = celdas;
        this.tam = tam;
    }

    @Override
    public void atacar(Army objetivo) {
        if (objetivo != null) {
            objetivo.recibirGolpe(daño);
        }
    }

    @Override
    public void run() {
        try {
            while (activo && this.getVida() > 0) {
                moverHaciaCentro();
                Thread.sleep(500); // velocidad del zombie
            }
        } catch (InterruptedException e) {
            System.out.println("Zombie interrumpido");
        }
    }

    private void moverHaciaCentro() {
        int centro = tam / 2;
        int deltaFila = Integer.compare(centro, fila);
        int deltaCol = Integer.compare(centro, columna);

        int nuevaFila = fila + deltaFila;
        int nuevaCol = columna + deltaCol;

        if (nuevaFila >= 0 && nuevaFila < tam && nuevaCol >= 0 && nuevaCol < tam) {
            Army objetivo = mapa[nuevaFila][nuevaCol];

            
            if (objetivo == null) {
               
                int antFila = fila;
                int antCol = columna;

                
                mapa[antFila][antCol] = null;
                celdas[antFila][antCol].setBackground(Color.WHITE);
                celdas[antFila][antCol].setText("");
                celdas[antFila][antCol].repaint();
                celdas[antFila][antCol].revalidate();

                
                fila = nuevaFila;
                columna = nuevaCol;
                mapa[fila][columna] = this;

                
                celdas[fila][columna].setBackground(this.getColor());
                celdas[fila][columna].setText(String.valueOf(this.getSimbolo()));
                celdas[fila][columna].repaint();
                celdas[fila][columna].revalidate();

            
            } else if (objetivo instanceof Reliquia || 
                       objetivo instanceof Defensa || 
                       objetivo instanceof Arma) {

                atacar(objetivo);
                if (!objetivo.estaVivo()) {
                    mapa[nuevaFila][nuevaCol] = null;
                    celdas[nuevaFila][nuevaCol].setBackground(Color.WHITE);
                    celdas[nuevaFila][nuevaCol].setText("");
                    celdas[nuevaFila][nuevaCol].repaint();
                    celdas[nuevaFila][nuevaCol].revalidate();
                }
            }
        }
    }

    public void detener() { activo = false; }
}

