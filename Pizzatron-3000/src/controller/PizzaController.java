package controller;

import model.*;

/**
 * Controla la construccion de la pizza en curso.
 * Opera el stack de Pizza: push al agregar, pop al deshacer.
 */
public class PizzaController {
    private Pizza pizzaEnConstruccion;

    public PizzaController() {
        this.pizzaEnConstruccion = new Pizza();
    }

    public void iniciarNuevaPizza() {
        this.pizzaEnConstruccion = new Pizza();
    }

    /** PUSH: agrega ingrediente al tope de la pila */
    public void agregarIngrediente(Ingrediente ing) {
        pizzaEnConstruccion.pushIngrediente(ing);
    }

    /** POP: quita el ultimo ingrediente (deshacer) */
    public Ingrediente deshacerUltimo() {
        return pizzaEnConstruccion.popIngrediente();
    }

    public Pizza getPizzaActual() {
        return pizzaEnConstruccion;
    }

    public boolean pizzaEstaVacia() {
        return pizzaEnConstruccion.estaVacia();
    }

    public Pizza enviarPizza() {
        pizzaEnConstruccion.armar();
        Pizza enviada = pizzaEnConstruccion;
        pizzaEnConstruccion = new Pizza();
        return enviada;
    }
}