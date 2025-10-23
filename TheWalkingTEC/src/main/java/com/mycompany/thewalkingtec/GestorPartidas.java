/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.thewalkingtec;

/**
 *
 * @author gabos
 */
import java.io.*;

public class GestorPartidas {

    public static void guardarPartida(Partida partida) {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("partida_" + partida.getJugador().getNombre() + ".dat"))) {
            out.writeObject(partida);
            System.out.println("✅ Partida guardada correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Partida cargarPartida(String nombreJugador) {
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream("partida_" + nombreJugador + ".dat"))) {
            Partida partida = (Partida) in.readObject();
            System.out.println("✅ Partida cargada de " + nombreJugador);
            return partida;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("⚠️ No se pudo cargar la partida de " + nombreJugador);
            return null;
        }
    }
}
