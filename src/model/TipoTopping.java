package model;

public enum TipoTopping {
    PESCADO("Pescado"),
    CAMARONES("Camarones"),
    SQUIDS("Squids"),
    ALGA("Alga"),
    HIELO("Hielo");

    private final String display;

    TipoTopping(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}