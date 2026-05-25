package model;

/**
 * Representa la masa de la pizza. Solo puede haber una por pizza.
 * Extiende Ingrediente y sobreescribe preparar() — polimorfismo.
 */
public class Base extends Ingrediente {
    private TipoBase tipo;

    public Base(TipoBase tipo) {
        super(tipo.getDisplay(), tipo == TipoBase.DELGADA ? 150 : 250);
        this.tipo = tipo;
    }

    public TipoBase getTipo() {
        return tipo;
    }

    @Override
    public void preparar() {
        System.out.println("  [COCINA] Preparando " + getNombre() + "...");
    }
}