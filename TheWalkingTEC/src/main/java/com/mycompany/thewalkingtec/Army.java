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
    protected String registro = "";
    protected int vidaInicial;

    public Army(int vida, int fuerza, Color color, char simbolo) {
        this.vida = vida;
        this.fuerza = fuerza;
        this.color = color;
        this.simbolo = simbolo;
        this.vidaInicial = vida;
    }

    public void iniciarRegistro() {
        this.vidaInicial = this.vida;
    }

    public void registrarAtaque(Army objetivo, int daño) {
        if (objetivo == null) return;
        registro += "Atacó a " + objetivo.getSimbolo() + "(" + objetivo.getFila() + "," + objetivo.getColumna() + ") con daño " + daño + "\n";
    }

    public void registrarRecibido(Army atacante, int daño) {
        if (atacante == null) return;
        registro += "Recibió ataque de " + atacante.getSimbolo() + "(" + atacante.getFila() + "," + atacante.getColumna() + ") por " + daño + "\n";
    }

    public String getResumen() {
        return """
            🧾 %s
            Vida inicial: %d
            Vida final: %d
            Registro de batalla:
            %s
            """.formatted(getSimbolo()+"("+getFila()+","+getColumna()+")",
                          vidaInicial, vida, registro.isEmpty() ? "Sin eventos" : registro);
    }

    public void recibirGolpe(int dano){
        vida -= dano;
    }
    public void recibirGolpe(int dano, Army atacante) {
    vida -= dano;
    if (atacante != null) {
        registrarRecibido(atacante, dano);
    }
}

    public boolean estaVivo() {
        return vida > 0;
    }

    public abstract void atacar(Army obj);
    public abstract int getFila();
    public abstract int getColumna();

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


