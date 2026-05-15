package controller;

import model.*;

/**
 * Genera ordenes y las encola en la Cocina.
 * Gestiona la ordenActual: unica orden que el chef atiende a la vez.
 */
public class OrdenController {
    private Cocina cocina;
    private Orden ordenActual;
    private int contadorId;

    public OrdenController(Cocina cocina) {
        this.cocina = cocina;
        this.contadorId = 1001;
        this.ordenActual = null;
    }

    public void generarOrdenes(int n) {
        String[][] templates = {
                { "Pedro", "GRUESA", "PICANTE", "CAMARONES", "ALGA" },
                { "Ana", "DELGADA", "NORMAL", "PESCADO", "SQUIDS" },
                { "James", "GRUESA", "NORMAL", "HIELO", "" },
                { "Xavi", "DELGADA", "PICANTE", "CAMARONES", "" },
                { "Messi", "GRUESA", "PICANTE", "PESCADO", "ALGA" },
                { "Gloria", "DELGADA", "NORMAL", "SQUIDS", "HIELO" },
        };

        for (int i = 0; i < n; i++) {
            String[] t = templates[i % templates.length];
            Pizza objetivo = new Pizza();

            objetivo.pushIngrediente(
                    new Base(t[1].equals("GRUESA") ? TipoBase.GRUESA : TipoBase.DELGADA));
            objetivo.pushIngrediente(new Salsa(t[2].equals("PICANTE")));
            if (!t[3].isEmpty())
                objetivo.pushIngrediente(new Topping(TipoTopping.valueOf(t[3])));
            if (!t[4].isEmpty())
                objetivo.pushIngrediente(new Topping(TipoTopping.valueOf(t[4])));

            Orden orden = new Orden(String.valueOf(contadorId++), t[0], objetivo);
            cocina.encolarOrden(orden);
            System.out.println("  Generada -> #" + orden.getId() +
                    " | Cliente: " + orden.getCliente() +
                    " | Estado: " + orden.getEstado());
        }
    }

    public boolean tomarSiguienteOrden() {
        if (cocina.estaVacia())
            return false;
        ordenActual = cocina.desencolarOrden();
        ordenActual.setEstado(EstadoOrden.EN_PROCESO);
        return true;
    }

    public Orden getOrdenActual() {
        return ordenActual;
    }

    public boolean hayOrdenActual() {
        return ordenActual != null;
    }

    public Cocina getCocina() {
        return cocina;
    }

    public void completarOrdenActual() {
        if (ordenActual != null) {
            ordenActual.setEstado(EstadoOrden.COMPLETADA);
            ordenActual = null;
        }
    }

    public void cancelarOrdenActual() {
        if (ordenActual != null) {
            ordenActual.setEstado(EstadoOrden.CANCELADA);
            ordenActual = null;
        }
    }
}