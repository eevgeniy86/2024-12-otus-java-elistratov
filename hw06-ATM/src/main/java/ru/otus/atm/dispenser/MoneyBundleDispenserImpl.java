package ru.otus.atm.dispenser;

import java.util.EnumMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.atm.nominal.NominalValue;

public class MoneyBundleDispenserImpl implements MoneyBundleDispenser {
    private static final Logger logger = LoggerFactory.getLogger(MoneyBundleDispenserImpl.class);
    private final Map<NominalValue, BanknotesDispenser> cartridges;

    public MoneyBundleDispenserImpl(Map<NominalValue, BanknotesDispenser> cartridges) {
        this.cartridges = cartridges;
    }

    @Override
    public void dispenseMoneyBundle(Map<NominalValue, Integer> moneyBundle) {
        for (Map.Entry<NominalValue, Integer> entry : moneyBundle.entrySet()) {
            cartridges.get(entry.getKey()).getBanknotes(entry.getValue());
            if (cartridges.get(entry.getKey()).getBanknotesQuantity() == 0) {
                logger.atWarn()
                        .setMessage("Banknotes of nominal {} run out")
                        .addArgument(entry.getKey().name())
                        .log();
            }
        }
    }

    @Override
    public Map<NominalValue, Integer> getRemainingMoneyBundle() {
        Map<NominalValue, Integer> remainingMoneyBundle = new EnumMap<>(NominalValue.class);
        for (Map.Entry<NominalValue, BanknotesDispenser> entry : cartridges.entrySet()) {
            remainingMoneyBundle.put(entry.getKey(), entry.getValue().getBanknotesQuantity());
        }
        return remainingMoneyBundle;
    }
}
