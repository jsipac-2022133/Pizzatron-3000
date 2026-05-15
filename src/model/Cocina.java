package model;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Usa Queue<Orden> (FIFO):
 * encolarOrden() -> nueva orden entra al final
 * desencolarOrden() -> primera orden sale para ser atendida
 */
public class Cocina {
    private Queue<Orden> colaOrdenes;

    public Cocina() {
        this.colaOrdenes = new LinkedList<>();
    }

    /** ENQUEUE: nueva orden entra al final de la cola */
    public void encolarOrden(Orden orden) {
        colaOrdenes.offer(orden);
    }

    /** DEQUEUE: saca y retorna la primera orden de la cola */
    public Orden desencolarOrden() {
        return colaOrdenes.poll();
    }

    public Orden verSiguiente() {
        return colaOrdenes.peek();
    }

    public boolean estaVacia() {
        return colaOrdenes.isEmpty();
    }

    public int getTamano() {
        return colaOrdenes.size();
    }

    public Queue<Orden> getColaOrdenes() {
        return colaOrdenes;
    }
}