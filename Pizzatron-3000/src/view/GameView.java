package view;

import model.*;
import controller.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GameView extends JFrame {

    // ── Controladores ────────────────────────────────────────────────────
    private OrdenController ordenCtrl;
    private PizzaController pizzaCtrl;
    private PuntuacionController puntosCtrl;

    // ── Colores ──────────────────────────────────────────────────────────
    private static final Color BG = new Color(30, 30, 35);
    private static final Color CARD = new Color(45, 45, 52);
    private static final Color PURPLE = new Color(83, 74, 183);
    private static final Color TEAL = new Color(29, 158, 117);
    private static final Color AMBER = new Color(186, 117, 23);
    private static final Color CORAL = new Color(153, 60, 29);
    private static final Color TEXT = new Color(194, 192, 182);
    private static final Color TEXT_DIM = new Color(120, 118, 110);
    private static final Color WHITE = Color.WHITE;

    // ── Componentes principales ──────────────────────────────────────────
    private JLabel timerLabel;
    private JLabel puntosLabel;
    private JLabel erroresLabel;
    private JTextArea ordenArea;
    private JTextArea colaArea;
    private JTextArea pilaArea;
    private JTextArea resultadoArea;
    private JPanel pizzaCanvas;

    // ── Timer ────────────────────────────────────────────────────────────
    private int segundos = 90;
    private javax.swing.Timer swingTimer;

    public GameView(OrdenController ordenCtrl, PizzaController pizzaCtrl,
            PuntuacionController puntosCtrl) {
        this.ordenCtrl = ordenCtrl;
        this.pizzaCtrl = pizzaCtrl;
        this.puntosCtrl = puntosCtrl;

        setTitle("Pizzatron 3000 — La Pizzeria de los Pinguinos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 680);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout(10, 10));

        add(buildHUD(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildBanda(), BorderLayout.SOUTH);

        iniciarTimer();
        setVisible(true);
    }

    // HUD SUPERIOR    
    private JPanel buildHUD() {
        JPanel hud = new JPanel(new GridLayout(1, 3, 10, 0));
        hud.setBackground(BG);
        hud.setBorder(new EmptyBorder(10, 10, 0, 10));

        // Timer + stats
        JPanel timerCard = card(PURPLE);
        timerCard.setLayout(new GridLayout(3, 1));
        timerLabel = hudLabel("1:30", 28, WHITE);
        puntosLabel = hudLabel("Puntos: 0", 13, TEXT);
        erroresLabel = hudLabel("Errores: 0", 13, TEXT);
        timerCard.add(centered(hudLabel("TIEMPO", 11, TEXT_DIM)));
        timerCard.add(centered(timerLabel));
        JPanel statsRow = new JPanel(new GridLayout(1, 2));
        statsRow.setOpaque(false);
        statsRow.add(centered(puntosLabel));
        statsRow.add(centered(erroresLabel));
        timerCard.add(statsRow);
        hud.add(timerCard);

        // Orden actual
        JPanel ordenCard = card(AMBER);
        ordenCard.setLayout(new BorderLayout(4, 4));
        ordenCard.add(hudLabel("  ORDEN ACTUAL", 11, TEXT_DIM), BorderLayout.NORTH);
        ordenArea = textArea();
        ordenArea.setText("Sin orden activa.\nUsa 'Tomar orden' para empezar.");
        ordenCard.add(new JScrollPane(ordenArea), BorderLayout.CENTER);
        hud.add(ordenCard);

        // Cola
        JPanel colaCard = card(CARD);
        colaCard.setLayout(new BorderLayout(4, 4));
        colaCard.add(hudLabel("  COLA DE ORDENES (FIFO)", 11, TEXT_DIM), BorderLayout.NORTH);
        colaArea = textArea();
        colaArea.setText("(cola vacía)");
        colaCard.add(new JScrollPane(colaArea), BorderLayout.CENTER);
        hud.add(colaCard);

        return hud;
    }

    // CENTRO: pizza canvas + pila + botones de acción
    private JPanel buildCenter() {
        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setBackground(BG);
        center.setBorder(new EmptyBorder(10, 10, 0, 10));

        // Pizza canvas (izquierda)
        pizzaCanvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarPizza((Graphics2D) g);
            }
        };
        pizzaCanvas.setBackground(CARD);
        pizzaCanvas.setBorder(BorderFactory.createLineBorder(PURPLE, 1));
        pizzaCanvas.setPreferredSize(new Dimension(260, 260));

        // Pila (centro)
        JPanel pilaCard = card(CARD);
        pilaCard.setLayout(new BorderLayout(4, 4));
        pilaCard.add(hudLabel("  PILA DE INGREDIENTES (STACK LIFO)", 11, TEXT_DIM), BorderLayout.NORTH);
        pilaArea = textArea();
        pilaArea.setText("(pila vacía)");
        pilaCard.add(new JScrollPane(pilaArea), BorderLayout.CENTER);

        // Resultado
        resultadoArea = textArea();
        resultadoArea.setForeground(TEAL);
        resultadoArea.setText("Aquí aparecerá el resultado al enviar la pizza.");
        JPanel resCard = card(CARD);
        resCard.setLayout(new BorderLayout(4, 4));
        resCard.add(hudLabel("  RESULTADO", 11, TEXT_DIM), BorderLayout.NORTH);
        resCard.add(new JScrollPane(resultadoArea), BorderLayout.CENTER);
        resCard.setPreferredSize(new Dimension(0, 80));

        // Botones de acción
        JPanel acciones = new JPanel(new GridLayout(2, 2, 8, 8));
        acciones.setBackground(BG);
        acciones.add(actionBtn("Generar órdenes", PURPLE, e -> accionGenerarOrdenes()));
        acciones.add(actionBtn("Tomar orden (DEQUEUE)", AMBER, e -> accionTomarOrden()));
        acciones.add(actionBtn("Deshacer (POP)", CORAL, e -> accionDeshacer()));
        acciones.add(actionBtn("Enviar pizza", TEAL, e -> accionEnviarPizza()));

        JPanel rightPanel = new JPanel(new BorderLayout(8, 8));
        rightPanel.setBackground(BG);
        rightPanel.add(pilaCard, BorderLayout.CENTER);
        rightPanel.add(resCard, BorderLayout.SOUTH);

        center.add(pizzaCanvas, BorderLayout.WEST);
        center.add(rightPanel, BorderLayout.CENTER);
        center.add(acciones, BorderLayout.EAST);

        return center;
    }

    // BANDA DE INGREDIENTES (SOUTH)
    private JPanel buildBanda() {
        JPanel banda = new JPanel(new BorderLayout(6, 6));
        banda.setBackground(CARD);
        banda.setBorder(new EmptyBorder(8, 10, 10, 10));

        JLabel title = hudLabel("BANDA DE INGREDIENTES — clic para agregar (PUSH)", 11, TEXT_DIM);
        banda.add(title, BorderLayout.NORTH);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        btns.setBackground(CARD);

        // Bases
        btns.add(ingBtn("Base Delgada", PURPLE, e -> pushIng(new Base(TipoBase.DELGADA))));
        btns.add(ingBtn("Base Gruesa", PURPLE, e -> pushIng(new Base(TipoBase.GRUESA))));
        // Salsas
        btns.add(ingBtn("Salsa Normal", CORAL, e -> pushIng(new Salsa(false))));
        btns.add(ingBtn("Salsa Picante", CORAL, e -> pushIng(new Salsa(true))));
        // Toppings
        btns.add(ingBtn("Pescado", TEAL, e -> pushIng(new Topping(TipoTopping.PESCADO))));
        btns.add(ingBtn("Camarones", TEAL, e -> pushIng(new Topping(TipoTopping.CAMARONES))));
        btns.add(ingBtn("Squids", TEAL, e -> pushIng(new Topping(TipoTopping.SQUIDS))));
        btns.add(ingBtn("Alga", TEAL, e -> pushIng(new Topping(TipoTopping.ALGA))));
        btns.add(ingBtn("Hielo", TEAL, e -> pushIng(new Topping(TipoTopping.HIELO))));

        banda.add(btns, BorderLayout.CENTER);
        return banda;
    }

    // ACCIONES
    private void accionGenerarOrdenes() {
        String input = JOptionPane.showInputDialog(this,
                "¿Cuántas órdenes generar? (1-6):", "Generar órdenes",
                JOptionPane.QUESTION_MESSAGE);
        if (input == null) {
            return;
        }
        try {
            int n = Math.max(1, Math.min(6, Integer.parseInt(input.trim())));
            ordenCtrl.generarOrdenes(n);
            actualizarCola();
            mostrarResultado("Se generaron " + n + " orden(es) en la cola.", TEAL);
        } catch (NumberFormatException ex) {
            mostrarResultado("Ingresa un número válido.", CORAL);
        }
    }

    private void accionTomarOrden() {
        if (ordenCtrl.hayOrdenActual()) {
            mostrarResultado("Ya hay una orden activa. Envíala primero.", CORAL);
            return;
        }
        boolean tomada = ordenCtrl.tomarSiguienteOrden();
        if (tomada) {
            actualizarOrden();
            actualizarCola();
            mostrarResultado("Orden tomada. ¡A armar la pizza!", TEAL);
        } else {
            mostrarResultado("Cola vacía. Genera órdenes primero.", CORAL);
        }
    }

    private void pushIng(Ingrediente ing) {
        if (!ordenCtrl.hayOrdenActual()) {
            mostrarResultado("Toma una orden primero.", CORAL);
            return;
        }
        pizzaCtrl.agregarIngrediente(ing);
        mostrarResultado("[PUSH] " + ing.getNombre() + " agregado.", TEAL);
        actualizarPila();
        pizzaCanvas.repaint();
    }

    private void accionDeshacer() {
        Ingrediente quitado = pizzaCtrl.deshacerUltimo();
        if (quitado == null) {
            mostrarResultado("La pila está vacía, nada que deshacer.", CORAL);
        } else {
            mostrarResultado("[POP] Se quitó: " + quitado.getNombre(), AMBER);
            actualizarPila();
            pizzaCanvas.repaint();
        }
    }

    private void accionEnviarPizza() {
        if (!ordenCtrl.hayOrdenActual()) {
            mostrarResultado("No hay orden activa.", CORAL);
            return;
        }
        if (pizzaCtrl.pizzaEstaVacia()) {
            mostrarResultado("La pizza está vacía, agrega ingredientes.", CORAL);
            return;
        }
        Pizza enviada = pizzaCtrl.enviarPizza();
        String res = puntosCtrl.compararPizzas(enviada,
                ordenCtrl.getOrdenActual().getPizzaObjetivo());
        ordenCtrl.completarOrdenActual();
        actualizarOrden();
        actualizarCola();
        actualizarPila();
        pizzaCanvas.repaint();
        puntosLabel.setText("Puntos: " + puntosCtrl.getPuntos());
        erroresLabel.setText("Errores: " + puntosCtrl.getErrores());
        boolean ok = res.startsWith("CORRECTA");
        mostrarResultado(res, ok ? TEAL : CORAL);
    }

    // ACTUALIZAR UI
    private void actualizarOrden() {
        Orden o = ordenCtrl.getOrdenActual();
        if (o == null) {
            ordenArea.setText("Sin orden activa.");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Orden #").append(o.getId())
                    .append(" | ").append(o.getCliente()).append("\n\nReceta:\n");
            Object[] ings = o.getPizzaObjetivo().getPilaIngredientes().toArray();
            for (int i = ings.length - 1; i >= 0; i--) {
                sb.append("  • ").append(((Ingrediente) ings[i]).getNombre()).append("\n");
            }
            ordenArea.setText(sb.toString());
            ordenArea.setCaretPosition(0);
        }
    }

    private void actualizarCola() {
        if (ordenCtrl.getCocina().estaVacia()) {
            colaArea.setText("(cola vacía)");
            return;
        }
        StringBuilder sb = new StringBuilder();
        int pos = 1;
        for (Orden o : ordenCtrl.getCocina().getColaOrdenes()) {
            sb.append(pos++).append(". #").append(o.getId())
                    .append(" ").append(o.getCliente()).append("\n");
        }
        colaArea.setText(sb.toString());
        colaArea.setCaretPosition(0);
    }

    private void actualizarPila() {
        Pizza p = pizzaCtrl.getPizzaActual();
        if (p.estaVacia()) {
            pilaArea.setText("(pila vacía)");
            return;
        }
        StringBuilder sb = new StringBuilder();
        Object[] ings = p.getPilaIngredientes().toArray();
        for (int i = ings.length - 1; i >= 0; i--) {
            sb.append(i == ings.length - 1 ? "► " : "  ");
            sb.append(((Ingrediente) ings[i]).getNombre());
            if (i == ings.length - 1) {
                sb.append("  ← TOPE");
            }
            sb.append("\n");
        }
        pilaArea.setText(sb.toString());
        pilaArea.setCaretPosition(0);
    }

    private void mostrarResultado(String msg, Color color) {
        resultadoArea.setForeground(color);
        resultadoArea.setText(msg);
    }

    // DIBUJAR PIZZA EN CANVAS
    private void dibujarPizza(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int cx = pizzaCanvas.getWidth() / 2;
        int cy = pizzaCanvas.getHeight() / 2;

        Pizza p = pizzaCtrl.getPizzaActual();
        if (p.estaVacia()) {
            g.setColor(TEXT_DIM);
            g.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g.drawString("Agrega una base", cx - 55, cy);
            return;
        }

        Object[] ings = p.getPilaIngredientes().toArray();
        boolean tieneBase = false;
        boolean tieneSalsa = false;
        java.util.List<String> tops = new ArrayList<>();

        for (Object o : ings) {
            Ingrediente ing = (Ingrediente) o;
            if (ing instanceof Base) {
                tieneBase = true;
            } else if (ing instanceof Salsa) {
                tieneSalsa = true;
            } else if (ing instanceof Topping) {
                tops.add(ing.getNombre());
            }
        }

        // Base (amarillo)
        if (tieneBase) {
            g.setColor(new Color(245, 196, 74));
            g.fillOval(cx - 90, cy - 90, 180, 180);
            g.setColor(new Color(212, 144, 10));
            g.setStroke(new BasicStroke(2));
            g.drawOval(cx - 90, cy - 90, 180, 180);
        }

        // Salsa (rojo)
        if (tieneSalsa) {
            g.setColor(new Color(192, 57, 27, 210));
            g.fillOval(cx - 72, cy - 72, 144, 144);
        }

        // Toppings
        Color[] topColors = {
            new Color(181, 212, 244),
            new Color(245, 196, 179),
            new Color(211, 209, 199),
            new Color(192, 221, 151),
            new Color(133, 183, 235)
        };
        int[][] positions = {{-35, -40}, {20, -45}, {-50, 10}, {30, 5}, {-15, 35}, {10, -15}};
        for (int i = 0; i < tops.size() && i < positions.length; i++) {
            Color c = topColors[i % topColors.length];
            g.setColor(c);
            g.fillOval(cx + positions[i][0], cy + positions[i][1], 30, 30);
            g.setColor(c.darker());
            g.setStroke(new BasicStroke(1));
            g.drawOval(cx + positions[i][0], cy + positions[i][1], 30, 30);
            g.setColor(new Color(40, 40, 40));
            g.setFont(new Font("SansSerif", Font.BOLD, 8));
            g.drawString(tops.get(i).substring(0, 3).toUpperCase(),
                    cx + positions[i][0] + 4, cy + positions[i][1] + 18);
        }
    }

    // TIMER
    private void iniciarTimer() {
        swingTimer = new javax.swing.Timer(1000, e -> {
            segundos--;
            int m = segundos / 60;
            int s = segundos % 60;
            timerLabel.setText(m + ":" + String.format("%02d", s));
            timerLabel.setForeground(segundos <= 20 ? CORAL : WHITE);
            if (segundos <= 0) {
                swingTimer.stop();
                timerLabel.setText("0:00");
                mostrarResultado("¡Tiempo agotado! Puntos finales: "
                        + puntosCtrl.getPuntos(), CORAL);
            }
        });
        swingTimer.start();
    }

    // HELPERS DE UI
    private JPanel card(Color bg) {
        JPanel p = new JPanel();
        p.setBackground(bg);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bg.darker(), 1),
                new EmptyBorder(6, 8, 6, 8)
        ));
        return p;
    }

    private JLabel hudLabel(String text, int size, Color color) {
        JLabel l = new JLabel(text);
        l.setForeground(color);
        l.setFont(new Font("SansSerif", Font.BOLD, size));
        return l;
    }

    private JTextArea textArea() {
        JTextArea ta = new JTextArea();
        ta.setBackground(new Color(38, 38, 44));
        ta.setForeground(TEXT);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
        ta.setEditable(false);
        ta.setBorder(new EmptyBorder(4, 6, 4, 6));
        return ta;
    }

    private JPanel centered(Component c) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 2));
        p.setOpaque(false);
        p.add(c);
        return p;
    }

    private JButton actionBtn(String text, Color color, ActionListener al) {
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(al);
        b.setPreferredSize(new Dimension(160, 50));
        return b;
    }

    private JButton ingBtn(String text, Color color, ActionListener al) {
        JButton b = new JButton(text);
        b.setBackground(color.darker());
        b.setForeground(WHITE);
        b.setFont(new Font("SansSerif", Font.PLAIN, 12));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(al);
        return b;
    }
}
