package ru.otus.atm.receiver;

import java.util.EnumMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.atm.nominal.NominalValue;

public class MoneyBundleReceiverImpl implements MoneyBundleReceiver {
    private static final Logger logger = LoggerFactory.getLogger(MoneyBundleReceiverImpl.class);

    private final Map<NominalValue, BanknotesReceiver> cartridges;

    public MoneyBundleReceiverImpl(Map<NominalValue, BanknotesReceiver> cartridges) {
        this.cartridges = cartridges;
    }

    @Override
    public void receiveMoneyBundle(Map<NominalValue, Integer> moneyBundle) {
        for (Map.Entry<NominalValue, Integer> entry : moneyBundle.entrySet()) {
            cartridges.get(entry.getKey()).putBanknotes(entry.getValue());
            if (cartridges.get(entry.getKey()).getRemainingSpaceForBanknotes() == 0) {
                logger.atWarn()
                        .setMessage("Free space for banknotes of nominal {} run out")
                        .addArgument(entry.getKey().name())
                        .log();
            }
        }
    }

    @Override
    public Map<NominalValue, Integer> getRemainingSpaceForMoneyBundle() {
        Map<NominalValue, Integer> remainingSpace = new EnumMap<>(NominalValue.class);
        for (Map.Entry<NominalValue, BanknotesReceiver> entry : cartridges.entrySet()) {
            remainingSpace.put(entry.getKey(), entry.getValue().getRemainingSpaceForBanknotes());
        }
        return remainingSpace;
    }
}
