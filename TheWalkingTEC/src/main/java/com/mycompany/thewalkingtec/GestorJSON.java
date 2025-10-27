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
import javax.swing.JButton;

public class GestorJSON {

    // ===================== GUARDAR =====================
    public static void guardarPartida(Jugador jugador, Army[][] mapa, int tam) {
        String nombreArchivo = "partida_" + jugador.getNombre() + ".json";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo))) {
            bw.write("{\n");
            bw.write("  \"jugador\": {\n");
            bw.write("    \"nombre\": \"" + jugador.getNombre() + "\",\n");
            bw.write("    \"nivel\": " + jugador.getNivelActual() + "\n");
            bw.write("  },\n");

            bw.write("  \"mapa\": [\n");
            for (int i = 0; i < tam; i++) {
                bw.write("    [");
                for (int j = 0; j < tam; j++) {
                    String simbolo = ".";
                    if (mapa[i][j] != null)
                        simbolo = String.valueOf(mapa[i][j].getSimbolo());
                    bw.write("\"" + simbolo + "\"");
                    if (j < tam - 1) bw.write(", ");
                }
                bw.write("]");
                if (i < tam - 1) bw.write(",");
                bw.write("\n");
            }
            bw.write("  ]\n");
            bw.write("}");
            System.out.println("✅ Partida guardada en " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("❌ Error al guardar partida: " + e.getMessage());
        }
    }

    // ===================== CARGAR JUGADOR =====================
    public static Jugador cargarJugador(String nombreArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            String nombre = "";
            int nivel = 1;

            while ((linea = br.readLine()) != null) {
                if (linea.contains("\"nombre\"")) {
                    nombre = linea.split(":")[1].replace("\"", "").replace(",", "").trim();
                } else if (linea.contains("\"nivel\"")) {
                    nivel = Integer.parseInt(linea.split(":")[1].replace(",", "").trim());
                }
            }

            Jugador j = new Jugador(nombre);
            j.setNivelActual(nivel);
            System.out.println("✅ Jugador cargado: " + nombre + " (Nivel " + nivel + ")");
            return j;

        } catch (IOException e) {
            System.out.println("⚠️ No se pudo cargar el archivo " + nombreArchivo);
            return null;
        }
    }

    // ===================== CARGAR MAPA =====================
    public static Army[][] cargarMapa(String nombreArchivo, int tam) {
        Army[][] mapa = new Army[tam][tam];
        boolean leyendoMapa = false;
        int fila = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                if (linea.startsWith("\"mapa\"")) {
                    leyendoMapa = true;
                    continue;
                }

                if (leyendoMapa && linea.startsWith("[")) {
                    linea = linea.replace("[", "")
                                 .replace("]", "")
                                 .replace("\"", "")
                                 .trim();

                    if (linea.endsWith(",")) linea = linea.substring(0, linea.length() - 1);

                    String[] simbolos = linea.split("\\s*,\\s*");

                    for (int col = 0; col < simbolos.length && col < tam; col++) {
                        String s = simbolos[col].trim();

                        switch (s) {
                            case "R" -> mapa[fila][col] = new Reliquia();
                            case "B" -> mapa[fila][col] = new Defensa();

                            // ---- ARMAS ----
                            case "C" -> mapa[fila][col] = new ArmaContacto(fila, col, mapa, new JButton[tam][tam], tam);
                            case "M" -> mapa[fila][col] = new ArmaMediano(fila, col, mapa, new JButton[tam][tam], tam);
                            case "A" -> mapa[fila][col] = new ArmaAerea(fila, col, mapa, new JButton[tam][tam], tam);
                            case "I" -> mapa[fila][col] = new ArmaImpacto(fila, col, mapa, new JButton[tam][tam], tam);
                            case "X" -> mapa[fila][col] = new ArmaMultiple(fila, col, mapa, new JButton[tam][tam], tam);

                            // ---- ZOMBIES ----
                            case "Z" -> mapa[fila][col] = new ZombieContacto(fila, col, mapa, new JButton[tam][tam], tam);
                            default -> mapa[fila][col] = null;
                        }
                    }

                    fila++;
                    if (fila >= tam) break;
                }
            }

            System.out.println("✅ Mapa cargado desde " + nombreArchivo + " (" + fila + " filas)");
        } catch (IOException e) {
            System.err.println("❌ Error al cargar mapa: " + e.getMessage());
        }

        return mapa;
    }
    // ===================== CARGAR MAPA =====================
public static ComponenteConfig leerComponente(String ruta) {
    ComponenteConfig cfg = new ComponenteConfig();

    try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            linea = linea.trim();
            if (!linea.contains(":")) continue;

            String[] partes = linea.split(":", 2);
            if (partes.length < 2) continue;

            String clave = partes[0].replace("\"", "").trim();
            String valor = partes[1].replace("\"", "").replace(",", "").trim();

            switch (clave) {
                case "id" -> cfg.id = valor;
                case "nombre" -> cfg.nombre = valor;
                case "imagenIdle" -> cfg.imagenIdle = valor;
                case "imagenAttack" -> cfg.imagenAttack = valor;
                case "vidaMax" -> cfg.vidaMax = Integer.parseInt(valor);
                case "golpesPorSegundo" -> cfg.golpesPorSegundo = Integer.parseInt(valor);
                case "espacios" -> cfg.espacios = Integer.parseInt(valor);
                case "nivelAparicion" -> cfg.nivelAparicion = Integer.parseInt(valor);
                case "nivel" -> cfg.nivel = Integer.parseInt(valor);
                case "_class" -> cfg._class = valor;
                case "tipo" -> cfg.tipo = Integer.parseInt(valor);
            }
        }

        System.out.println("✅ Componente cargado: " + cfg.nombre + " (" + cfg._class + ") tipo=" + cfg.tipo);
    } catch (IOException e) {
        System.err.println("❌ Error al leer componente: " + e.getMessage());
    } catch (NumberFormatException nfe) {
        System.err.println("⚠️ Error de formato numérico en JSON: " + nfe.getMessage());
    }

    return cfg;}
}


