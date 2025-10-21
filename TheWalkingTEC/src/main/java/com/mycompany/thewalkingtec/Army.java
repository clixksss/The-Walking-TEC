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
public abstract class Army {
    private int vida;

    private int fuerza;
    private Color color;
    private char simbolo;

    public Army(int vida, int fuerza, Color color, char simbolo) {
        this.vida = vida;
        this.fuerza = fuerza;
        this.color = color;
        this.simbolo = simbolo;
    }

    public void recibirGolpe(int dano){
        vida -= dano;
    }
    public boolean estaVivo() {
        return vida > 0;
    }
    public abstract void atacar(Army obj);

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getFuerza() {
        return fuerza;
    }

    public void setFuerza(int fuerza) {
        this.fuerza = fuerza;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public char getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(char simbolo) {
        this.simbolo = simbolo;
    }
    
           
    
}

