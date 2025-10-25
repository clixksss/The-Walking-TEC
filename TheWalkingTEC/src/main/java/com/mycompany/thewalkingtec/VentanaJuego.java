/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.thewalkingtec;
import javax.swing.*;
import java.awt.*;


/**
 *
 * @author gabos
 */
public class VentanaJuego extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaJuego.class.getName());
    private static final int TAM = 25;
    private Army[][] mapaObjetos = new Army[TAM][TAM];
    private JButton[][] celdas = new JButton[TAM][TAM];
    private java.util.List<Thread> hilosZombies = new java.util.ArrayList<>();
    private boolean modoBatalla = false;
    private Jugador jugador;


    /**
     * Creates new form Mapa
     */
    public VentanaJuego(Jugador jugador) {
        initComponents();
        this.jugador = jugador;
        configurarUI();         
        inicializarNuevoJuego();
    }

    public VentanaJuego(Jugador jugador, Army[][] mapaCargado) {
        initComponents();
        this.jugador = jugador;
        this.mapaObjetos = mapaCargado;

        configurarUI();
        construirMapa();
        refrescarMapaVisual();

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                guardarPartidaAutomatica();
            }
        });
    }
private void guardarPartidaAutomatica() {
    if (jugador == null) return;
    try {
        GestorJSON.guardarPartida(jugador, mapaObjetos, TAM);
        System.out.println("Partida guardada automáticamente: " + jugador.getNombre());
    } catch (Exception e) {
        System.err.println("️ Error al guardar automáticamente: " + e.getMessage());
    }
}
    private void configurarUI() {
        panelMapa.setLayout(new GridLayout(TAM, TAM, 1, 1));
        panelMapa.setBackground(Color.DARK_GRAY);
        buttonGroup1.add(togDefensa);
        buttonGroup1.add(togBorrar);
        buttonGroup1.add(togReliquia);
        buttonGroup1.add(togArmas);
        togDefensa.setSelected(true);

        if (btnLimpiar != null) {
            btnLimpiar.addActionListener(e -> limpiarMapa());
        }
        }
    private void construirMapa() {
        panelMapa.removeAll();
        panelMapa.setLayout(new GridLayout(TAM, TAM, 1, 1));
        panelMapa.setBackground(Color.DARK_GRAY);

        for (int f = 0; f < TAM; f++) {
            for (int c = 0; c < TAM; c++) {
                JButton b = new JButton();
                b.setMargin(new Insets(0, 0, 0, 0));
                b.setFocusable(false);
                b.setBackground(Color.WHITE);
                b.setToolTipText("(" + f + "," + c + ")");

                final int fila = f, col = c;

                b.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mousePressed(java.awt.event.MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            colocarSegunSeleccion(fila, col);
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            mostrarInfoComponente(fila, col);
                        }
                    }
                });

                celdas[f][c] = b;
                panelMapa.add(b);
            }
        }

        panelMapa.revalidate();
        panelMapa.repaint();
    }
    private void mostrarResumenFinal() {
        StringBuilder sb = new StringBuilder(" RESULTADO FINAL\n\n");

        for (int f = 0; f < TAM; f++) {
            for (int c = 0; c < TAM; c++) {
                Army a = mapaObjetos[f][c];
                if (a != null) {
                    sb.append(a.getResumen()).append("\n");
                }
            }
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(600, 400));
        JOptionPane.showMessageDialog(this, scroll, "Resumen de Batalla", JOptionPane.INFORMATION_MESSAGE);
    }


private void inicializarNuevoJuego() {
    construirMapa();
    colocarReliquiaCentro();
    configurarUI();
    JOptionPane.showMessageDialog(this, "Nivel " + jugador.getNivelActual() + " iniciado.");
}
private void reconstruirMapa() {
    construirMapa();
    refrescarMapaVisual();
    JOptionPane.showMessageDialog(this, "Partida de " + jugador.getNombre() + " cargada (Nivel " + jugador.getNivelActual() + ")");
}
public void refrescarMapaVisual() {
    for (int f = 0; f < TAM; f++) {
        for (int c = 0; c < TAM; c++) {
            JButton boton = celdas[f][c];
            Army elemento = mapaObjetos[f][c];

            if (elemento == null) {
                boton.setText("");
                boton.setBackground(Color.WHITE);
            } 
            else {
                boton.setText(String.valueOf(elemento.getSimbolo()));

                if (elemento instanceof Reliquia) {
                    boton.setBackground(new Color(0, 180, 0)); // Verde oscuiro
                } 
                else if (elemento instanceof Defensa) {
                    boton.setBackground(new Color(120, 120, 120)); // Gris defensa
                }
                else if (elemento instanceof ArmaContacto) {
                    boton.setBackground(new Color(60, 200, 60)); // Verde
                } 
                else if (elemento instanceof ArmaMediano) {
                    boton.setBackground(new Color(240, 220, 70)); // Amarillo
                } 
                else if (elemento instanceof ArmaAerea) {
                    boton.setBackground(new Color(120, 180, 255)); // Azul claro
                } 
                else if (elemento instanceof ArmaImpacto) {
                    boton.setBackground(new Color(255, 140, 60)); // Naranja
                } 
                else if (elemento instanceof ArmaMultiple) {
                    boton.setBackground(new Color(170, 100, 255)); // Morado
                }
                // ---- ZOMBIES ----
                else if (elemento instanceof ZombieContacto) {
                    boton.setBackground(new Color(220, 60, 60)); // Rojo
                } 
                else {
                    boton.setBackground(Color.LIGHT_GRAY);
                }
            }

            boton.repaint();
            boton.revalidate();
        }
    }

    panelMapa.repaint();
    panelMapa.revalidate();
}

private void colocarSegunSeleccion(int f, int c) {
    if (modoBatalla) return;

    JButton b = celdas[f][c];

    
    if (togBorrar.isSelected()) {
        mapaObjetos[f][c] = null;
        b.setText("");
        b.setBackground(Color.WHITE);
        return;
    }

    Army nuevo = null;

    
    if (togReliquia.isSelected()) {
        quitarReliquiaExistente();
        nuevo = new Reliquia();
    }

    else if (togDefensa.isSelected()) {
        if (mapaObjetos[f][c] instanceof Reliquia) return;
        nuevo = new Defensa(); 
    }

    else if (togArmas.isSelected()) {
        if (mapaObjetos[f][c] instanceof Reliquia) return;

        String[] opciones = {
            "De contacto (Cuerpo a cuerpo)",
            "De mediano alcance",
            "Aérea",
            "De impacto (mina)",
            "De ataque múltiple"
        };

        String seleccion = (String) JOptionPane.showInputDialog(
            this,
            "Selecciona el tipo de arma:",
            "Tipo de arma",
            JOptionPane.PLAIN_MESSAGE,
            null,
            opciones,
            opciones[0]
        );

        if (seleccion == null) return; 

        switch (seleccion) {
            case "De contacto (Cuerpo a cuerpo)" ->
                nuevo = new ArmaContacto(f, c, mapaObjetos, celdas, TAM);
            case "De mediano alcance" ->
                nuevo = new ArmaMediano(f, c, mapaObjetos, celdas, TAM);
            case "Aérea" ->
                nuevo = new ArmaAerea(f, c, mapaObjetos, celdas, TAM);
            case "De impacto (mina)" ->
                nuevo = new ArmaImpacto(f, c, mapaObjetos, celdas, TAM);
            case "De ataque múltiple" ->
                nuevo = new ArmaMultiple(f, c, mapaObjetos, celdas, TAM);
        }
    }

    if (nuevo != null) {
        mapaObjetos[f][c] = nuevo;
        b.setText(String.valueOf(nuevo.getSimbolo()));
        b.setBackground(nuevo.getColor());
        b.repaint();
        b.revalidate();
    }
}



private boolean quitarReliquiaExistente() {
    boolean quitada = false;
    for (int f = 0; f < TAM; f++) {
        for (int c = 0; c < TAM; c++) {
            if (mapaObjetos[f][c] instanceof Reliquia) {
                mapaObjetos[f][c] = null;
                JButton b = celdas[f][c];
                b.setText("");
                b.setBackground(Color.WHITE);
                quitada = true;
            }
        }
    }
    return quitada;
}
private void colocarReliquiaCentro() {
    int centro = TAM / 2;
    mapaObjetos[centro][centro] = new Reliquia();
    JButton b = celdas[centro][centro];
    b.setText("R");
    b.setBackground(new Color(0,180,0));
    b.setToolTipText("Reliquia ("+centro+","+centro+") Vida:50");
}
private void mostrarInfoComponente(int f, int c) {
    Army elemento = mapaObjetos[f][c];
    if (elemento == null) {
        JOptionPane.showMessageDialog(this, "Celda vacía");
        return;
    }

    JOptionPane.showMessageDialog(this, elemento.getResumen(),
        "Información de " + elemento.getClass().getSimpleName(),
        JOptionPane.INFORMATION_MESSAGE);
}


private void limpiarMapa() {
    for (int f = 0; f < TAM; f++) {
        for (int c = 0; c < TAM; c++) {
            mapaObjetos[f][c] = null;
            JButton b = celdas[f][c];
            b.setText("");
            b.setBackground(Color.WHITE);
            b.setToolTipText("("+f+","+c+")");
        }
    }
    colocarReliquiaCentro();
}
private void generarZombies() {
    int nivel = jugador.getNivelActual();
    int cantidad = 5 + nivel;
    java.util.Random random = new java.util.Random();

    for (int i = 0; i < cantidad; i++) {
        int fila, col;
        int lado = random.nextInt(4);
        if (lado == 0) { fila = 0; col = random.nextInt(TAM); }
        else if (lado == 1) { fila = TAM - 1; col = random.nextInt(TAM); }
        else if (lado == 2) { fila = random.nextInt(TAM); col = 0; }
        else { fila = random.nextInt(TAM); col = TAM - 1; }

        // seleccionar tipo permitido
        Zombie z;
        if (nivel < 3) z = new ZombieContacto(fila, col, mapaObjetos, celdas, TAM);
        else if (nivel < 5) z = random.nextBoolean()
                ? new ZombieContacto(fila, col, mapaObjetos, celdas, TAM)
                : new ZombieAereo(fila, col, mapaObjetos, celdas, TAM);
        else if (nivel < 7) {
            int tipo = random.nextInt(3);
            z = switch (tipo) {
                case 0 -> new ZombieContacto(fila, col, mapaObjetos, celdas, TAM);
                case 1 -> new ZombieAereo(fila, col, mapaObjetos, celdas, TAM);
                default -> new ZombieMediano(fila, col, mapaObjetos, celdas, TAM);
            };
        } else {
            int tipo = random.nextInt(4);
            z = switch (tipo) {
                case 0 -> new ZombieContacto(fila, col, mapaObjetos, celdas, TAM);
                case 1 -> new ZombieAereo(fila, col, mapaObjetos, celdas, TAM);
                case 2 -> new ZombieMediano(fila, col, mapaObjetos, celdas, TAM);
                default -> new ZombieChoque(fila, col, mapaObjetos, celdas, TAM);
            };
        }

        mapaObjetos[fila][col] = z;
        JButton b = celdas[fila][col];
        b.setText(String.valueOf(z.getSimbolo()));
        b.setBackground(z.getColor());

        Thread hilo = new Thread(z);
        hilo.start();
        hilosZombies.add(hilo);
    }
}

private void activarArmas() {
    for (int f = 0; f < TAM; f++) {
        for (int c = 0; c < TAM; c++) {
            if (mapaObjetos[f][c] instanceof Arma) {
                Arma arma = (Arma) mapaObjetos[f][c];
                // Iniciar el hilo de disparo
                new Thread(arma).start();
            }
        }
    }
}

private void bloquearEdicion(boolean activarBatalla) {
    modoBatalla = activarBatalla;
}
private boolean hayZombiesVivos() {
    for (int f = 0; f < TAM; f++) {
        for (int c = 0; c < TAM; c++) {
            if (mapaObjetos[f][c] instanceof Zombie) {
                return true;
            }
        }
    }
    return false;
}
private void nivelCompletado() {
    jugador.subirNivel();
    JOptionPane.showMessageDialog(this,
        "¡Nivel superado!\nAvanzas al nivel " + jugador.getNivelActual());
    mostrarResumenFinal();
    GestorJSON.guardarPartida(jugador, mapaObjetos, TAM);

    aumentarDificultad();

    limpiarMapa();

    bloquearEdicion(false);
}

private void aumentarDificultad() {
    int nivel = jugador.getNivelActual();

    Zombie.setVidaBase(100 + nivel * 20);
    Zombie.setDañoBase(5 + nivel * 2);

    Arma.setDañoBase(5 + nivel * 3);

    System.out.println(" Nivel " + nivel + ": zombies más fuertes, armas más potentes.");
}






    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        panelMapa = new javax.swing.JPanel();
        panelControles = new javax.swing.JPanel();
        togDefensa = new javax.swing.JToggleButton();
        togBorrar = new javax.swing.JToggleButton();
        togReliquia = new javax.swing.JToggleButton();
        btnLimpiar = new javax.swing.JButton();
        togArmas = new javax.swing.JToggleButton();
        btnZombies = new javax.swing.JButton();
        btnGuardarPartida = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelMapa.setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout panelMapaLayout = new javax.swing.GroupLayout(panelMapa);
        panelMapa.setLayout(panelMapaLayout);
        panelMapaLayout.setHorizontalGroup(
            panelMapaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 593, Short.MAX_VALUE)
        );
        panelMapaLayout.setVerticalGroup(
            panelMapaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 574, Short.MAX_VALUE)
        );

        togDefensa.setText("Defensa");
        togDefensa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                togDefensaActionPerformed(evt);
            }
        });

        togBorrar.setText("Borrar");

        togReliquia.setText("Reliquia");
        togReliquia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                togReliquiaActionPerformed(evt);
            }
        });

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        togArmas.setText("Armas");
        togArmas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                togArmasActionPerformed(evt);
            }
        });

        btnZombies.setText("Zombies");
        btnZombies.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZombiesActionPerformed(evt);
            }
        });

        btnGuardarPartida.setText("Guardar Partida");
        btnGuardarPartida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarPartidaActionPerformed(evt);
            }
        });

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelControlesLayout = new javax.swing.GroupLayout(panelControles);
        panelControles.setLayout(panelControlesLayout);
        panelControlesLayout.setHorizontalGroup(
            panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlesLayout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelControlesLayout.createSequentialGroup()
                        .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(togBorrar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(togDefensa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(togReliquia, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(togArmas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelControlesLayout.createSequentialGroup()
                        .addComponent(btnGuardarPartida)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelControlesLayout.createSequentialGroup()
                        .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnZombies)
                                .addComponent(btnLimpiar))
                            .addGroup(panelControlesLayout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addGap(4, 4, 4)))
                        .addGap(21, 21, 21))))
        );
        panelControlesLayout.setVerticalGroup(
            panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlesLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(togDefensa)
                .addGap(18, 18, 18)
                .addComponent(togBorrar)
                .addGap(18, 18, 18)
                .addComponent(togReliquia)
                .addGap(18, 18, 18)
                .addComponent(togArmas)
                .addGap(18, 18, 18)
                .addComponent(btnZombies)
                .addGap(18, 18, 18)
                .addComponent(btnLimpiar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 155, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnGuardarPartida)
                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelMapa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelControles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(panelMapa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelControles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void togReliquiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_togReliquiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_togReliquiaActionPerformed

    private void togDefensaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_togDefensaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_togDefensaActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarMapa();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void togArmasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_togArmasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_togArmasActionPerformed

    private void btnZombiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZombiesActionPerformed
        bloquearEdicion(true);
        generarZombies();
        activarArmas();
    new Thread(() -> {
        try {
            while (true) {
                Thread.sleep(2000); 
                if (!hayZombiesVivos()) {
                    nivelCompletado();
                    break;
                }
            }
        } catch (InterruptedException ignored) {}
    }).start();
    }//GEN-LAST:event_btnZombiesActionPerformed

    private void btnGuardarPartidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarPartidaActionPerformed
    if (jugador == null) {
        JOptionPane.showMessageDialog(this, "⚠️ No hay ningún jugador activo para guardar.");
        return;
    }

    try {
        GestorJSON.guardarPartida(jugador, mapaObjetos, TAM);
        JOptionPane.showMessageDialog(this, 
            "💾 Partida guardada correctamente.\nJugador: " + jugador.getNombre() +
            "\nNivel: " + jugador.getNivelActual());
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
            "❌ Error al guardar la partida:\n" + e.getMessage());
        e.printStackTrace();
    }
    }//GEN-LAST:event_btnGuardarPartidaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        mostrarResumenFinal();
    }//GEN-LAST:event_jButton1ActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardarPartida;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnZombies;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel panelControles;
    private javax.swing.JPanel panelMapa;
    private javax.swing.JToggleButton togArmas;
    private javax.swing.JToggleButton togBorrar;
    private javax.swing.JToggleButton togDefensa;
    private javax.swing.JToggleButton togReliquia;
    // End of variables declaration//GEN-END:variables
}
