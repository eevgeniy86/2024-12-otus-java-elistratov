package ru.otus.atm.receiver;

import java.util.Map;
import ru.otus.atm.nominal.NominalValue;

public interface MoneyBundleReceiver {

    void receiveMoneyBundle(Map<NominalValue, Integer> moneyBundle);

    Map<NominalValue, Integer> getRemainingSpaceForMoneyBundle();
}
