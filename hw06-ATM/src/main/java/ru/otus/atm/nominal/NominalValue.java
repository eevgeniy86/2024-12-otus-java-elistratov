package ru.otus.atm.nominal;

public enum NominalValue {
    FIFTY(50),
    HUNDRED(100),
    FIVE_HUNDRED(500),
    THOUSAND(1000),
    FIVE_THOUSAND(5000);

    private final int value;

    NominalValue(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
