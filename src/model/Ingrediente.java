package model;

/**
 * Clase abstracta base de la jerarquia de ingredientes.
 * Implementa Cocinable y centraliza nombre y calorias.
 * Nunca se instancia directamente.
 */
public abstract class Ingrediente implements Cocinable {
    private String nombre;
    private int calorias;

    public Ingrediente(String nombre, int calorias) {
        this.nombre = nombre;
        this.calorias = calorias;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public int getCalorias() {
        return calorias;
    }

    @Override
    public String toString() {
        return nombre + " (" + calorias + " cal)";
    }
}