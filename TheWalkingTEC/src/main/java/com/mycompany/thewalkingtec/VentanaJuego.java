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
            b.setMargin(new Insets(0,0,0,0));
            b.setFocusable(false);
            b.setBackground(Color.WHITE);
            b.setToolTipText("(" + f + "," + c + ")");

            final int fila = f, col = c;
            b.addActionListener(evt -> colocarSegunSeleccion(fila, col));

            celdas[f][c] = b;
            panelMapa.add(b);
        }
    }

    panelMapa.revalidate();
    panelMapa.repaint();
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
            } else {
                boton.setText(String.valueOf(elemento.getSimbolo()));
                boton.setBackground(elemento.getColor());
            }

            boton.repaint();
            boton.revalidate();
        }
    }
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
        nuevo = new Arma(f, c, mapaObjetos, celdas, TAM);  
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
    int cantidad = 5; 
    java.util.Random random = new java.util.Random();

    for (int i = 0; i < cantidad; i++) {
        int fila, col;

        // Zombies en bordes
        int lado = random.nextInt(4);
        if (lado == 0) { fila = 0; col = random.nextInt(TAM); }          // arriba
        else if (lado == 1) { fila = TAM - 1; col = random.nextInt(TAM);} // abajo
        else if (lado == 2) { fila = random.nextInt(TAM); col = 0; }      // izquierda
        else { fila = random.nextInt(TAM); col = TAM - 1; }               // derecha

        // Crear el zombie y ponerlo en el mapa
        Zombie z = new Zombie(fila, col, mapaObjetos, celdas, TAM);
        mapaObjetos[fila][col] = z;

        JButton b = celdas[fila][col];
        b.setText("Z");
        b.setBackground(z.getColor());

        // Hilo
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
                        .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnZombies)
                            .addComponent(btnLimpiar))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 194, Short.MAX_VALUE)
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



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardarPartida;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnZombies;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel panelControles;
    private javax.swing.JPanel panelMapa;
    private javax.swing.JToggleButton togArmas;
    private javax.swing.JToggleButton togBorrar;
    private javax.swing.JToggleButton togDefensa;
    private javax.swing.JToggleButton togReliquia;
    // End of variables declaration//GEN-END:variables
}
