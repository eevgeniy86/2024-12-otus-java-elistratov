package ru.otus.atm.dispenser;

public class DispensingCartridge implements BanknotesDispenser {
    private int quantity;

    public DispensingCartridge(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public void getBanknotes(int quantity) {
        this.quantity -= quantity;
    }

    @Override
    public int getBanknotesQuantity() {

        return quantity;
    }
}
