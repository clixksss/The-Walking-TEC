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
public class ZombieMediano extends Zombie {

    private int alcance = 3;

    public ZombieMediano(int f, int c, Army[][] m, JButton[][] celdas, int tam) {
        super(90, 5, new Color(255, 220, 60), 'M', f, c, m, celdas, tam, 700, 5);
    }
    public ZombieMediano(int vida,int dano,int nivelApa,int f, int c, Army[][] m, JButton[][] celdas, int tam) {
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
        int distancia = Math.max(Math.abs(fila - centro), Math.abs(columna - centro));

        if (distancia > alcance) {
            int df = Integer.compare(centro, fila);
            int dc = Integer.compare(centro, columna);
            moverA(fila + df, columna + dc);
        } else {
            atacar(mapa[centro][centro]);
            celdas[fila][columna].setBackground(new Color(255, 255, 180));
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            celdas[fila][columna].setBackground(getColor());
        }
    }
    @Override
    public int getFila() { return fila; }
    @Override
    public int getColumna() { return columna; }
}
