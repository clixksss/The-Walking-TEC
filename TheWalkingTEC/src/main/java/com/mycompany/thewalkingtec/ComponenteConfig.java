/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.thewalkingtec;

/**
 *
 * @author gabos
 */
public class ComponenteConfig {
    public String id;
    public String nombre;
    public String imagenIdle;
    public String imagenAttack;
    public int vidaMax;
    public int golpesPorSegundo;
    public int espacios;
    public int nivelAparicion;
    public int nivel;
    public String _class;
    public int tipo;

    @Override
    public String toString() {
        return nombre + " (" + _class + ")";
    }
    
}
