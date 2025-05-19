package ru.otus.atm.dispenser;

import java.util.Map;
import ru.otus.atm.nominal.NominalValue;

public interface MoneyBundleDispenser {
    void dispenseMoneyBundle(Map<NominalValue, Integer> moneyBundle);

    Map<NominalValue, Integer> getRemainingMoneyBundle();
}
