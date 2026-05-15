package model;

/**
 * Representa el pedido de un cliente.
 * Guarda la pizza objetivo (receta correcta) y el estado actual.
 */
public class Orden {
    private String id;
    private String cliente;
    private Pizza pizzaObjetivo;
    private EstadoOrden estado;

    public Orden(String id, String cliente, Pizza pizzaObjetivo) {
        this.id = id;
        this.cliente = cliente;
        this.pizzaObjetivo = pizzaObjetivo;
        this.estado = EstadoOrden.PENDIENTE;
    }

    public String getId() {
        return id;
    }

    public String getCliente() {
        return cliente;
    }

    public Pizza getPizzaObjetivo() {
        return pizzaObjetivo;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden e) {
        this.estado = e;
    }

    @Override
    public String toString() {
        return "Orden #" + id + " | Cliente: " + cliente +
                " | Estado: " + estado + "\n" +
                "  Receta objetivo:\n" + pizzaObjetivo.toString();
    }
}