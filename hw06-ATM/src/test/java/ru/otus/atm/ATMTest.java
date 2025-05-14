package ru.otus.atm;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.EnumMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.atm.dispenser.BanknotesDispenser;
import ru.otus.atm.dispenser.DispensingCartridge;
import ru.otus.atm.dispenser.MoneyBundleDispenser;
import ru.otus.atm.dispenser.MoneyBundleDispenserImpl;
import ru.otus.atm.nominal.NominalValue;
import ru.otus.atm.receiver.BanknotesReceiver;
import ru.otus.atm.receiver.MoneyBundleReceiver;
import ru.otus.atm.receiver.MoneyBundleReceiverImpl;
import ru.otus.atm.receiver.ReceivingCartridge;

class ATMTest {
    ATM atm;
    Map<NominalValue, Integer> moneyToPut;
    String moneyReceivedMessage = "your money received";
    String moneyNotReceivedMessage = "can't receive such a lot of money";
    String moneyNotDispensedMessage = "incorrect sum";
    String moneyDispensedMessage = "take your money";

    @BeforeEach
    void setUp() {
        Map<NominalValue, BanknotesDispenser> dispenserCartridges = new EnumMap<>(NominalValue.class);
        Map<NominalValue, BanknotesReceiver> receiverCartridges = new EnumMap<>(NominalValue.class);

        for (NominalValue nominal : NominalValue.values()) {
            dispenserCartridges.put(nominal, new DispensingCartridge(1));
            receiverCartridges.put(nominal, new ReceivingCartridge(1));
        }
        MoneyBundleDispenser dispenser = new MoneyBundleDispenserImpl(dispenserCartridges);
        MoneyBundleReceiver receiver = new MoneyBundleReceiverImpl(receiverCartridges);

        atm = new ATM(dispenser, receiver);

        moneyToPut = new EnumMap<>(NominalValue.class);
        moneyToPut.put(NominalValue.FIFTY, 1);
    }

    @Test
    @DisplayName("Can get money rest in ATM")
    void getMoneyRestTest() {
        assertThat(atm.getMoneyRest()).isEqualTo(6650);
    }

    @Test
    @DisplayName("Can put money in ATM")
    void receiveMoneyTest() {
        assertThat(atm.receiveMoney(moneyToPut)).isEqualTo(moneyReceivedMessage);
    }

    @Test
    @DisplayName("Can't put more money than volume in ATM")
    void notReceiveToMuchMoneyTest() {
        assertThat(atm.receiveMoney(moneyToPut)).isEqualTo(moneyReceivedMessage);
        assertThat(atm.receiveMoney(moneyToPut)).isEqualTo(moneyNotReceivedMessage);
    }

    @Test
    @DisplayName("Can't get money of wrong nominal from ATM")
    void notDispenseWrongNominalMoneyTest() {
        assertThat(atm.dispenseMoney(5)).isEqualTo(moneyNotDispensedMessage);
    }

    @Test
    @DisplayName("Can get money from ATM")
    void dispenseMoneyTest() {
        assertThat(atm.dispenseMoney(50)).isEqualTo(moneyDispensedMessage);
    }

    @Test
    @DisplayName("Can't get to much money from ATM")
    void notDispenseToMuchMoneyTest() {
        atm.dispenseMoney(50);
        assertThat(atm.dispenseMoney(50)).isEqualTo(moneyNotDispensedMessage);
    }

    @Test
    @DisplayName("Dispensing money reduces rest in the ATM")
    void dispenseMoneyReducesRestTest() {
        atm.dispenseMoney(1150);
        assertThat(atm.getMoneyRest()).isEqualTo(5500);
    }
}
