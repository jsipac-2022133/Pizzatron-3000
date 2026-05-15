package controller;

import model.*;
import java.util.*;

/**
 * Compara la pizza armada con la pizza objetivo de la orden.
 */
public class PuntuacionController {
    private int puntos;
    private int errores;
    private Stack<String> historialResultados;

    public PuntuacionController() {
        this.puntos = 0;
        this.errores = 0;
        this.historialResultados = new Stack<>();
    }

    /**
     * Compara ingredientes de la pizza hecha vs la pizza objetivo.
     * Hace PUSH del resultado al historial.
     */
    public String compararPizzas(Pizza hecha, Pizza objetivo) {
        List<String> hechos = extraerNombres(hecha);
        List<String> objetivos = extraerNombres(objetivo);

        List<String> faltantes = new ArrayList<>(objetivos);
        faltantes.removeAll(hechos);

        List<String> sobran = new ArrayList<>(hechos);
        sobran.removeAll(objetivos);

        String resultado;

        if (faltantes.isEmpty() && sobran.isEmpty()) {
            puntos += 100;
            resultado = "CORRECTA! +100 puntos. Pizza perfecta!";
        } else {
            errores++;
            StringBuilder sb = new StringBuilder("INCORRECTA. ");
            if (!faltantes.isEmpty())
                sb.append("Te falto: ").append(String.join(", ", faltantes)).append(". ");
            if (!sobran.isEmpty())
                sb.append("Sobraba: ").append(String.join(", ", sobran)).append(".");
            resultado = sb.toString().trim();
        }

        historialResultados.push(resultado); // PUSH al historial
        return resultado;
    }

    private List<String> extraerNombres(Pizza p) {
        List<String> nombres = new ArrayList<>();
        for (Object ing : p.getPilaIngredientes().toArray()) {
            nombres.add(((Ingrediente) ing).getNombre());
        }
        return nombres;
    }

    public String verUltimoResultado() {
        if (historialResultados.isEmpty())
            return "(sin resultados aun)";
        return historialResultados.peek();
    }

    public void mostrarHistorial() {
        if (historialResultados.isEmpty()) {
            System.out.println("  (historial vacio)");
            return;
        }
        Object[] arr = historialResultados.toArray();
        for (int i = arr.length - 1; i >= 0; i--) {
            System.out.println("  " + (arr.length - i) + ". " + arr[i]);
        }
    }

    public int getPuntos() {
        return puntos;
    }

    public int getErrores() {
        return errores;
    }

    public void resetear() {
        puntos = 0;
        errores = 0;
        historialResultados.clear();
    }
}