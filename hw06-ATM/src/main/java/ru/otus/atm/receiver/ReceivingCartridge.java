package ru.otus.atm.receiver;

public class ReceivingCartridge implements BanknotesReceiver {
    private final int volume;
    private int quantity;

    public ReceivingCartridge(int volume) {
        this.volume = volume;
        this.quantity = 0;
    }

    @Override
    public void putBanknotes(int quantity) {
        this.quantity += quantity;
    }

    @Override
    public int getRemainingSpaceForBanknotes() {
        return volume - quantity;
    }
}
