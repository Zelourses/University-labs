package ru.domen;

public enum MentalHealth {
    NORMAL("В норме"),
    WORRIED("Взволнован");
    private String state;
    MentalHealth(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return state;
    }
}
