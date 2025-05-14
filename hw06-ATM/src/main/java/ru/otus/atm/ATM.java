package ru.otus.atm;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.atm.dispenser.MoneyBundleDispenser;
import ru.otus.atm.nominal.NominalValue;
import ru.otus.atm.receiver.MoneyBundleReceiver;

public class ATM {
    private final MoneyBundleDispenser dispenser;
    private final MoneyBundleReceiver receiver;
    private static final Logger logger = LoggerFactory.getLogger(ATM.class);

    public ATM(MoneyBundleDispenser dispenser, MoneyBundleReceiver receiver) {
        this.dispenser = dispenser;
        this.receiver = receiver;
    }

    public int getMoneyRest() {

        Map<NominalValue, Integer> remainingMoneyBundle = dispenser.getRemainingMoneyBundle();
        int moneyRest = 0;

        for (Map.Entry<NominalValue, Integer> entry : remainingMoneyBundle.entrySet()) {

            moneyRest += entry.getKey().value() * entry.getValue();
        }
        logger.atInfo()
                .setMessage("Requested money rest, sum: {}")
                .addArgument(moneyRest)
                .log();

        return moneyRest;
    }

    public String dispenseMoney(int money) {
        int rest = money;
        Map<NominalValue, Integer> remainingMoneyBundle = dispenser.getRemainingMoneyBundle();
        Map<NominalValue, Integer> result = new EnumMap<>(NominalValue.class);
        List<NominalValue> nominals = new ArrayList<>(remainingMoneyBundle.keySet());
        nominals.sort(Comparator.comparing(NominalValue::value).reversed());
        for (NominalValue nominal : nominals) {
            if (remainingMoneyBundle.get(nominal) > 0 && rest / nominal.value() > 0) {
                if (remainingMoneyBundle.get(nominal) >= rest / nominal.value()) {
                    result.put(nominal, rest / nominal.value());
                    rest -= (rest / nominal.value()) * nominal.value();
                } else {
                    result.put(nominal, remainingMoneyBundle.get(nominal));
                    rest -= remainingMoneyBundle.get(nominal) * nominal.value();
                }
            }
        }
        if (rest > 0) {
            logger.atInfo()
                    .setMessage("Requested incorrect sum: {}")
                    .addArgument(money)
                    .log();
            return "incorrect sum";
        } else {
            dispenser.dispenseMoneyBundle(result);
            logger.atInfo()
                    .setMessage("Dispensed sum: {}, money bundle {}")
                    .addArgument(money)
                    .addArgument(result)
                    .log();
            return "take your money";
        }
    }

    public String receiveMoney(Map<NominalValue, Integer> moneyBundle) {
        Map<NominalValue, Integer> remainingSpaceForMoneyBundle = receiver.getRemainingSpaceForMoneyBundle();
        for (Map.Entry<NominalValue, Integer> entry : moneyBundle.entrySet()) {
            if (remainingSpaceForMoneyBundle.get(entry.getKey()) < entry.getValue()) {
                logger.atInfo()
                        .setMessage("Received money bundle can't be stored for nominal: {}, quantity {}")
                        .addArgument(entry.getKey())
                        .addArgument(entry.getValue())
                        .log();
                return "can't receive such a lot of money";
            }
        }
        receiver.receiveMoneyBundle(moneyBundle);
        logger.atInfo()
                .setMessage("Received money bundle: {}")
                .addArgument(moneyBundle)
                .log();
        return "your money received";
    }
}
