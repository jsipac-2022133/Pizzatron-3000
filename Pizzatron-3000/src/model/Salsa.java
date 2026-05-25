package model;

/**
 * Representa la salsa de la pizza: normal o picante.
 * Extiende Ingrediente y sobreescribe preparar() — polimorfismo.
 */
public class Salsa extends Ingrediente {
    private boolean picante;

    public Salsa(boolean picante) {
        super(picante ? "Salsa Picante" : "Salsa Normal", picante ? 80 : 60);
        this.picante = picante;
    }

    public boolean esPicante() {
        return picante;
    }

    @Override
    public void preparar() {
        System.out.println("  [COCINA] Aplicando " + getNombre() + "...");
    }
}