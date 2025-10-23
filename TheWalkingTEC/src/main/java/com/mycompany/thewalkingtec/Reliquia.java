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
public class Reliquia extends Army {
    
    public Reliquia() {
        super(1, 0, new Color(0,128,0), 'R');
    }
    
    public Reliquia(int fila, int columna, Army[][] mapa, JButton[][] celdas, int tam) {
        this();
    }
    public void atacar(Army obj){
        
    }
    
    
}
