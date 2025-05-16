package ru.otus.atm;

import java.util.EnumMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.atm.dispenser.BanknotesDispenser;
import ru.otus.atm.dispenser.DispensingCartridge;
import ru.otus.atm.dispenser.MoneyBundleDispenser;
import ru.otus.atm.dispenser.MoneyBundleDispenserImpl;
import ru.otus.atm.nominal.NominalValue;
import ru.otus.atm.receiver.BanknotesReceiver;
import ru.otus.atm.receiver.MoneyBundleReceiver;
import ru.otus.atm.receiver.MoneyBundleReceiverImpl;
import ru.otus.atm.receiver.ReceivingCartridge;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        Map<NominalValue, BanknotesDispenser> dispenserCartridges = new EnumMap<>(NominalValue.class);
        Map<NominalValue, BanknotesReceiver> receiverCartridges = new EnumMap<>(NominalValue.class);

        for (NominalValue nominal : NominalValue.values()) {
            dispenserCartridges.put(nominal, new DispensingCartridge(1));
            receiverCartridges.put(nominal, new ReceivingCartridge(1));
        }
        MoneyBundleDispenser dispenser = new MoneyBundleDispenserImpl(dispenserCartridges);
        MoneyBundleReceiver receiver = new MoneyBundleReceiverImpl(receiverCartridges);

        ATM atm = new ATM(dispenser, receiver);

        Map<NominalValue, Integer> res = new EnumMap<>(NominalValue.class);
        res.put(NominalValue.FIFTY, 1);

        logger.atInfo().setMessage("Rest: {}").addArgument(atm.getMoneyRest()).log();

        try {
            atm.receiveMoney(res);
            atm.receiveMoney(res);
            atm.dispenseMoney(5);
            atm.dispenseMoney(50);
            atm.dispenseMoney(1100);
            atm.dispenseMoney(50);
            logger.atInfo()
                    .setMessage("Rest: {}")
                    .addArgument(atm.getMoneyRest())
                    .log();
        } catch (Exception e) {
            logger.atError().setMessage(e.getClass() + ": " + e.getMessage()).log();
        }
    }
}
