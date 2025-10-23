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
public class Defensa extends Army{

    public Defensa() {
        super(100, 0, new Color(60,120,220), 'D');
    }
    public Defensa(int fila, int columna, Army[][] mapa, JButton[][] celdas, int tam) {
        this();
    }

    @Override
    public void atacar(Army obj) {
        
    }
    
}
