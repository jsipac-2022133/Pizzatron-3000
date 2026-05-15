package model;

public enum TipoBase {
    DELGADA("Base Delgada"),
    GRUESA("Base Gruesa");

    private final String display;

    TipoBase(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}