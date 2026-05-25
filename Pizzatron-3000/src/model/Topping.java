package model;

/**
 * Representa un topping: pescado, camarones, squids, alga o hielo.
 * Puede haber multiples toppings por pizza.
 * Extiende Ingrediente y sobreescribe preparar() — polimorfismo.
 */
public class Topping extends Ingrediente {
    private TipoTopping tipo;

    public Topping(TipoTopping tipo) {
        super(tipo.getDisplay(), 50);
        this.tipo = tipo;
    }

    public TipoTopping getTipo() {
        return tipo;
    }

    @Override
    public void preparar() {
        System.out.println("  [COCINA] Colocando topping: " + getNombre() + "...");
    }
}