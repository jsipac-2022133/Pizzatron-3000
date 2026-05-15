package model;

/**
 * Interfaz que define el contrato de cualquier ingrediente.
 * Permite polimorfismo: la cocina trata Base, Salsa y Topping de forma
 * uniforme.
 */
public interface Cocinable {
    void preparar();

    String getNombre();

    int getCalorias();
}