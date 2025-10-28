/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.thewalkingtec;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;



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
private Army crearArmaDesdeConfig(ComponenteConfig cfg, int f, int c) {
    if (cfg == null || cfg._class == null) return null;

    // tipo == 0 → arma, tipo == 1 → zombie
    if (cfg.tipo == 0) {
        return switch (cfg._class) {
            case "Component_creator.Contacto" -> new ArmaContacto(cfg.vidaMax,cfg.golpesPorSegundo,1,f, c, mapaObjetos, celdas, TAM);
            case "Component_creator.Mediano" -> new ArmaMediano(cfg.vidaMax,cfg.golpesPorSegundo,4,f, c, mapaObjetos, celdas, TAM);
            case "Component_creator.Aereo" -> new ArmaAerea(cfg.vidaMax,cfg.golpesPorSegundo,10,f, c, mapaObjetos, celdas, TAM);
            case "Component_creator.Impacto" -> new ArmaImpacto(cfg.vidaMax,cfg.golpesPorSegundo,1,f, c, mapaObjetos, celdas, TAM);
            case "Component_creator.Multiple" -> new ArmaMultiple(cfg.vidaMax,cfg.golpesPorSegundo,5,f, c, mapaObjetos, celdas, TAM);
            default -> {
                System.err.println("⚠️ Tipo de arma no reconocido: " + cfg._class);
                yield null;
            }
        };
    } 
    else {
        return switch (cfg._class) {
            case "Component_creator.Contacto" -> new ZombieContacto(cfg.vidaMax,cfg.golpesPorSegundo,cfg.nivelAparicion,f, c, mapaObjetos, celdas, TAM);
            case "Component_creator.Mediano" -> new ZombieMediano(cfg.vidaMax,cfg.golpesPorSegundo,cfg.nivelAparicion,f, c, mapaObjetos, celdas, TAM);
            case "Component_creator.Aereo" -> new ZombieAereo(cfg.vidaMax,cfg.golpesPorSegundo,cfg.nivelAparicion,f, c, mapaObjetos, celdas, TAM);
            case "Component_creator.Choque" -> new ZombieChoque(cfg.vidaMax,cfg.golpesPorSegundo,cfg.nivelAparicion,f, c, mapaObjetos, celdas, TAM);
            default -> {
                System.err.println("⚠️ Tipo de zombie no reconocido: " + cfg._class);
                yield null;
            }
        };
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

    int defensasActuales = contarDefensas();
    int maxDefensas = jugador.getCapacidadDefensas(); 

    if (defensasActuales >= maxDefensas) {
        JOptionPane.showMessageDialog(this,
            "⚠️ Has alcanzado el límite de defensas para este nivel (" + maxDefensas + ").",
            "Límite alcanzado",
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    nuevo = new Defensa();
}

    else if (togArmas.isSelected()) {
        if (mapaObjetos[f][c] instanceof Reliquia) return;

        String[] opciones = {
            "De contacto (Cuerpo a cuerpo)",
            "De mediano alcance",
            "Aérea",
            "De impacto (mina)",
            "De ataque múltiple",
            "📂 Cargar desde archivo JSON"
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
            case "📂 Cargar desde archivo JSON" -> {
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle("Seleccionar componente JSON");
                if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    File archivo = fc.getSelectedFile();
                    ComponenteConfig comp = GestorJSON.leerComponente(archivo.getAbsolutePath());
                    if (comp != null) {
                        nuevo = crearArmaDesdeConfig(comp, f, c);
                    }
                }
            }
        }
    }

    if (nuevo != null) {
        mapaObjetos[f][c] = nuevo;
        b.setText(String.valueOf(nuevo.getSimbolo()));
        b.setBackground(nuevo.getColor());
        b.repaint();
        b.revalidate();
        actualizarContadorDefensas();
    }
}

private void actualizarContadorDefensas() {
    int actuales = contarDefensas();
    int max = jugador.getCapacidadDefensas();
    lblContadorDefensas.setText("Defensas: " + actuales + " / " + max);
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
private int contarZombies() {
    int total = 0;
    for (int f = 0; f < TAM; f++)
        for (int c = 0; c < TAM; c++)
            if (mapaObjetos[f][c] instanceof Zombie)
                total++;
    return total;
}

private void generarZombies() {
    for (int f = 0; f < TAM; f++) {
    for (int c = 0; c < TAM; c++) {
        if (mapaObjetos[f][c] instanceof Zombie) return;
    }
}
    int nivel = jugador.getNivelActual();
    int capacidad = 20 + (nivel - 1) * 5; // mismos espacios que el fuerte
    java.util.Random random = new java.util.Random();

    java.util.List<Integer> tiposDisponibles = new java.util.ArrayList<>();
    if (nivel >= ZombieContacto.getNivelAparicion()) tiposDisponibles.add(0);
    if (nivel >= ZombieMediano.getNivelAparicion()) tiposDisponibles.add(1);
    if (nivel >= ZombieAereo.getNivelAparicion()) tiposDisponibles.add(2);
    if (nivel >= ZombieChoque.getNivelAparicion()) tiposDisponibles.add(3);

    if (tiposDisponibles.isEmpty()) {
        System.out.println("⚠️ No hay tipos de zombies disponibles para este nivel.");
        return;
    }

    int creados = 0;
    int intentos = 0;

    while (creados < capacidad && intentos < capacidad * 10) {
        intentos++;

        int fila, col;
        int lado = random.nextInt(4);

        // Zombies aparecen solo en los bordes
        if (lado == 0) { fila = 0; col = random.nextInt(TAM); }
        else if (lado == 1) { fila = TAM - 1; col = random.nextInt(TAM); }
        else if (lado == 2) { fila = random.nextInt(TAM); col = 0; }
        else { fila = random.nextInt(TAM); col = TAM - 1; }

        // Evitar superposición
        if (mapaObjetos[fila][col] != null) continue;

        int tipo = tiposDisponibles.get(random.nextInt(tiposDisponibles.size()));
        Zombie z = switch (tipo) {
            case 0 -> new ZombieContacto(100, 1, nivel, fila, col, mapaObjetos, celdas, TAM);
            case 1 -> new ZombieMediano(100, 1, nivel, fila, col, mapaObjetos, celdas, TAM);
            case 2 -> new ZombieAereo(100, 1, nivel, fila, col, mapaObjetos, celdas, TAM);
            default -> new ZombieChoque(100, 1, nivel, fila, col, mapaObjetos, celdas, TAM);
        };

        mapaObjetos[fila][col] = z;
        celdas[fila][col].setText(String.valueOf(z.getSimbolo()));
        celdas[fila][col].setBackground(z.getColor());
        Thread hilo = new Thread(z);
        hilo.start();
        hilosZombies.add(hilo);

        creados++;
    }

    System.out.println("Zombies normales generados: " + creados + " (nivel " + nivel + ")");

    File carpetaZombies = new File("C:\\Users\\gabos\\OneDrive\\Documentos\\NetBeansProjects\\The-Walking-TEC\\TheWalkingTEC\\Zombies");
    java.util.ArrayList<ComponenteConfig> zombiesJSON = GestorJSON.cargarZombiesDisponibles(carpetaZombies, nivel);

    if (zombiesJSON != null && !zombiesJSON.isEmpty()) {
        System.out.println("Agregando zombies personalizados desde JSON: " + zombiesJSON.size());
        int agregados = 0;

        for (ComponenteConfig cfg : zombiesJSON) {
            if (cfg.nivelAparicion > nivel) continue;

            int fila, col, intentosJson = 0;
            do {
                fila = random.nextInt(TAM);
                col = random.nextInt(TAM);
                intentosJson++;
            } while (mapaObjetos[fila][col] != null && intentosJson < 50);

            if (mapaObjetos[fila][col] != null) continue;

            Army nuevo = crearArmaDesdeConfig(cfg, fila, col);
            if (nuevo instanceof Zombie z) {
                mapaObjetos[fila][col] = z;
                celdas[fila][col].setText(String.valueOf(z.getSimbolo()));
                celdas[fila][col].setBackground(z.getColor());
                new Thread(z).start();
                agregados++;
            }
        }

        System.out.println("Zombies personalizados agregados: " + agregados);
    } else {
        System.out.println("️ No se encontraron zombies personalizados para nivel " + nivel + ".");
    }

    System.out.println(" Total zombies en el mapa: " + contarZombies());
}

private boolean hayReliquia() {
    for (int f = 0; f < TAM; f++) {
        for (int c = 0; c < TAM; c++) {
            Army obj = mapaObjetos[f][c];
            if (obj instanceof Reliquia && obj.estaVivo()) {
                return true;
            }
        }
    }
    return false;
}
private void iniciarMonitorDeReliquia() {
    new Thread(() -> {
        try {
            while (true) {
                Thread.sleep(500); // revisar cada medio segundo

                if (!hayReliquia()) {
                    SwingUtilities.invokeLater(() -> gameOver());
                    break;
                }
            }
        } catch (InterruptedException ignored) {}
    }).start();
}
private void gameOver() {
    for (int f = 0; f < TAM; f++) {
        for (int c = 0; c < TAM; c++) {
            Army obj = mapaObjetos[f][c];
            if (obj instanceof Zombie z) {
                z.detener();
            }
        }
    }

    modoBatalla = false;

    int opcion = JOptionPane.showOptionDialog(
        this,
        " ¡La reliquia ha sido destruida!\n\n¿Qué deseas hacer?",
        "Fin del nivel",
        JOptionPane.YES_NO_CANCEL_OPTION,
        JOptionPane.ERROR_MESSAGE,
        null,
        new Object[]{"Reintentar nivel", "➡️ Siguiente nivel", "❌ Salir"},
        " Reintentar nivel"
    );

    switch (opcion) {
        case 0 -> { 
            int nivelActual = jugador.getNivelActual(); 
            JOptionPane.showMessageDialog(this, "Reiniciando nivel " + nivelActual + "...");
            limpiarZombiesAnteriores();
            limpiarMapa();
            jugador.setNivelActual(nivelActual); 
            colocarReliquiaCentro();
            refrescarMapaVisual();
        }

        case 1 -> { 
            siguienteNivel(); 
        }

        default -> { 
            JOptionPane.showMessageDialog(this, "Gracias por jugar The Walking TEC️");
            System.exit(0);
        }
    }
}


private int contarDefensas() {
    int total = 0;
    for (int f = 0; f < TAM; f++) {
        for (int c = 0; c < TAM; c++) {
            if (mapaObjetos[f][c] instanceof Defensa) {
                total++;
            }
        }
    }
    return total;
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



private void limpiarZombiesAnteriores() {
    for (int f = 0; f < TAM; f++) {
        for (int c = 0; c < TAM; c++) {
            if (mapaObjetos[f][c] instanceof Zombie z) {
                z.detener(); // pone activo=false
            }
        }
    }

    for (Thread t : hilosZombies) {
        if (t != null && t.isAlive()) {
            t.interrupt();
            try { t.join(100); } catch (InterruptedException ignored) {}
        }
    }
    hilosZombies.clear();

    for (int f = 0; f < TAM; f++) {
        for (int c = 0; c < TAM; c++) {
            if (mapaObjetos[f][c] instanceof Zombie) {
                mapaObjetos[f][c] = null;
                celdas[f][c].setText("");
                celdas[f][c].setBackground(Color.WHITE);
            }
        }
    }
}

private void siguienteNivel() {
    limpiarZombiesAnteriores();

    jugador.setNivelActual(jugador.getNivelActual() + 1);
    int nivel = jugador.getNivelActual();
    System.out.println("🚀 Avanzando al nivel " + nivel);

    for (int f = 0; f < TAM; f++) {
        for (int c = 0; c < TAM; c++) {
            mapaObjetos[f][c] = null;
            celdas[f][c].setText("");
            celdas[f][c].setBackground(Color.WHITE);
        }
    }

    int centro = TAM / 2;
    Reliquia r = new Reliquia();
    mapaObjetos[centro][centro] = r;
    celdas[centro][centro].setText(String.valueOf(r.getSimbolo()));
    celdas[centro][centro].setBackground(r.getColor());

    modoBatalla = false;
    refrescarMapaVisual();
    actualizarContadorDefensas();

    iniciarMonitorDeReliquia();

    JOptionPane.showMessageDialog(
        this,
        "🔥 Nivel " + nivel + " iniciado 🔥",
        "Nuevo nivel",
        JOptionPane.INFORMATION_MESSAGE
    );
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
        btnSigNivel = new javax.swing.JButton();
        lblContadorDefensas = new javax.swing.JLabel();

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

        jButton1.setText("Resultados");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnSigNivel.setText("Siguiente Nivel");
        btnSigNivel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSigNivelActionPerformed(evt);
            }
        });

        lblContadorDefensas.setText("Defensas: 0 / 0");

        javax.swing.GroupLayout panelControlesLayout = new javax.swing.GroupLayout(panelControles);
        panelControles.setLayout(panelControlesLayout);
        panelControlesLayout.setHorizontalGroup(
            panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelControlesLayout.createSequentialGroup()
                        .addComponent(btnGuardarPartida)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelControlesLayout.createSequentialGroup()
                        .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnZombies)
                                .addComponent(btnLimpiar))
                            .addGroup(panelControlesLayout.createSequentialGroup()
                                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnSigNivel)
                                    .addComponent(jButton1))
                                .addGap(4, 4, 4)))
                        .addGap(21, 21, 21))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelControlesLayout.createSequentialGroup()
                        .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(togBorrar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(togDefensa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(togReliquia, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(togArmas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelControlesLayout.createSequentialGroup()
                        .addComponent(lblContadorDefensas, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        panelControlesLayout.setVerticalGroup(
            panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlesLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(lblContadorDefensas, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
                .addComponent(btnSigNivel)
                .addGap(37, 37, 37)
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
        iniciarMonitorDeReliquia();
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

    private void togReliquiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_togReliquiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_togReliquiaActionPerformed

    private void btnSigNivelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSigNivelActionPerformed
        siguienteNivel();
    }//GEN-LAST:event_btnSigNivelActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardarPartida;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnSigNivel;
    private javax.swing.JButton btnZombies;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel lblContadorDefensas;
    private javax.swing.JPanel panelControles;
    private javax.swing.JPanel panelMapa;
    private javax.swing.JToggleButton togArmas;
    private javax.swing.JToggleButton togBorrar;
    private javax.swing.JToggleButton togDefensa;
    private javax.swing.JToggleButton togReliquia;
    // End of variables declaration//GEN-END:variables
}
