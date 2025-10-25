/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.thewalkingtec;

/**
 *
 * @author gabos
 */

import java.util.ArrayList;
import java.util.List;

public class RegistroCombate {
    private final String id;            
    private final String tipo;          
    private final int vidaInicial;
    private int vidaFinal;

    private final List<String> atacantes = new ArrayList<>();
    private final List<String> atacados = new ArrayList<>();
    private final List<Integer> dañoCausado = new ArrayList<>();
    private final List<Integer> dañoRecibido = new ArrayList<>();

    public RegistroCombate(String id, String tipo, int vidaInicial) {
        this.id = id;
        this.tipo = tipo;
        this.vidaInicial = vidaInicial;
        this.vidaFinal = vidaInicial;
    }

    public void registrarAtaque(String objetivo, int daño) {
        atacados.add(objetivo);
        dañoCausado.add(daño);
    }

    public void registrarRecibido(String atacante, int daño) {
        atacantes.add(atacante);
        dañoRecibido.add(daño);
        vidaFinal -= daño;
        if (vidaFinal < 0) vidaFinal = 0;
    }

    public String getResumen() {
        return """
            🧾 %s (%s)
            Vida inicial: %d
            Vida final: %d
            Atacó a: %s
            Daño causado: %s
            Fue atacado por: %s
            Daño recibido: %s
            """.formatted(id, tipo, vidaInicial, vidaFinal,
            atacados.isEmpty() ? "Ninguno" : atacados,
            dañoCausado.isEmpty() ? "Ninguno" : dañoCausado,
            atacantes.isEmpty() ? "Nadie" : atacantes,
            dañoRecibido.isEmpty() ? "Ninguno" : dañoRecibido);
    }

    public int getVidaFinal() { return vidaFinal; }
    public String getId() { return id; }
    public String getTipo() { return tipo; }
}

