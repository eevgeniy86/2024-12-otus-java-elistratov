package ru.otus.atm.receiver;

public interface BanknotesReceiver {
    void putBanknotes(int quantity);

    int getRemainingSpaceForBanknotes();
}
