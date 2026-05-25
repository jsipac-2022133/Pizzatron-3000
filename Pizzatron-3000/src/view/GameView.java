package view;

import model.*;
import controller.*;
import java.util.Scanner;

public class GameView {
    private Scanner scanner;

    public GameView() {
        this.scanner = new Scanner(System.in);
    }

    // ── Menus ──────────────────────────────────────────────────────────────

    public void mostrarBienvenida() {
        separador();
        System.out.println("    BIENVENIDO A LA PIZZERIA DE LOS PINGUINOS");
        System.out.println("           Pizzatron 3000");
        separador();
    }

    public int mostrarMenuPrincipal() {
        System.out.println("\nMENU PRINCIPAL:");
        System.out.println("  1. Generar nuevas ordenes");
        System.out.println("  2. Tomar siguiente orden de la cola");
        System.out.println("  3. Armar pizza");
        System.out.println("  4. Ver orden actual");
        System.out.println("  5. Ver cola de ordenes");
        System.out.println("  6. Ver historial de resultados");
        System.out.println("  7. Resetear puntuacion");
        System.out.println("  8. Salir");
        System.out.print("\nIngresa tu opcion (1-8): ");
        return leerEntero();
    }

    public int mostrarMenuIngredientes() {
        System.out.println("\n--- ARMAR PIZZA ---");
        System.out.println("BASES:");
        System.out.println("  1. Base Delgada");
        System.out.println("  2. Base Gruesa");
        System.out.println("SALSAS:");
        System.out.println("  3. Salsa Normal");
        System.out.println("  4. Salsa Picante");
        System.out.println("TOPPINGS:");
        System.out.println("  5. Pescado");
        System.out.println("  6. Camarones");
        System.out.println("  7. Squids");
        System.out.println("  8. Alga");
        System.out.println("  9. Hielo");
        System.out.println("ACCIONES:");
        System.out.println("  10. Deshacer ultimo ingrediente (POP)");
        System.out.println("  11. Ver pizza actual (pila)");
        System.out.println("  12. Enviar pizza");
        System.out.println("   0. Volver al menu principal");
        System.out.print("\nIngresa tu opcion: ");
        return leerEntero();
    }

    public void mostrarOrdenActual(Orden orden) {
        if (orden == null) {
            System.out.println("\n[!] No hay orden activa. Usa opcion 2 para tomar una.");
            return;
        }
        System.out.println("\n--- ORDEN ACTUAL ---");
        System.out.println(orden);
    }

    public void mostrarPizzaActual(Pizza pizza) {
        System.out.println("\n--- PIZZA EN CONSTRUCCION (pila LIFO) ---");
        if (pizza.estaVacia()) {
            System.out.println("  (pila vacia, agrega ingredientes)");
        } else {
            System.out.print(pizza);
        }
    }

    public void mostrarCola(Cocina cocina) {
        System.out.println("\n--- COLA DE ORDENES (FIFO) ---");
        if (cocina.estaVacia()) {
            System.out.println("  (cola vacia)");
            return;
        }
        int pos = 1;
        for (Orden o : cocina.getColaOrdenes()) {
            System.out.println("  " + pos++ + ". Orden #" + o.getId() +
                    " | " + o.getCliente() +
                    " | " + o.getEstado());
        }
    }

    public void mostrarPuntuacion(int puntos, int errores) {
        System.out.println("\n--- PUNTUACION ---");
        System.out.println("  Puntos : " + puntos);
        System.out.println("  Errores: " + errores);
    }

    public void mostrarResultado(String resultado) {
        separador();
        System.out.println("  RESULTADO: " + resultado);
        separador();
    }

    public void mostrarMensaje(String msg) {
        System.out.println("\n" + msg);
    }

    public void mostrarError(String msg) {
        System.out.println("\n[ERROR] " + msg);
    }

    public int pedirCantidadOrdenes() {
        System.out.print("Cuantas ordenes generar? (1-6): ");
        int n = leerEntero();
        return Math.max(1, Math.min(6, n));
    }

    private int leerEntero() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void separador() {
        System.out.println("------------------------------------------------");
    }

    public void cerrar() {
        scanner.close();
    }
}