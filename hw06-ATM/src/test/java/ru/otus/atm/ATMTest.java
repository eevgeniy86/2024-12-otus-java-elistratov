package ru.otus.atm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    String moneyNotReceivedErrorMessage = "Received money bundle can't be stored";
    String moneyNotDispensedErrorMessage = "Incorrect sum requested";
    Map<NominalValue, BanknotesReceiver> receiverCartridges;
    Map<NominalValue, BanknotesDispenser> dispenserCartridges;
    int receiverCartridgeVolume = 1;
    int dispenserCartridgeQuantity = 1;

    @BeforeEach
    void setUp() {
        dispenserCartridges = new EnumMap<>(NominalValue.class);
        receiverCartridges = new EnumMap<>(NominalValue.class);

        for (NominalValue nominal : NominalValue.values()) {
            dispenserCartridges.put(nominal, new DispensingCartridge(dispenserCartridgeQuantity));
            receiverCartridges.put(nominal, new ReceivingCartridge(receiverCartridgeVolume));
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
        atm.receiveMoney(moneyToPut);

        for (Map.Entry<NominalValue, Integer> entry : moneyToPut.entrySet()) {
            assertThat(receiverCartridges.get(entry.getKey()).getRemainingSpaceForBanknotes())
                    .isEqualTo(receiverCartridgeVolume - entry.getValue());
        }
    }

    @Test
    @DisplayName("Can't put more money than volume in ATM")
    void notReceiveToMuchMoneyTest() {
        atm.receiveMoney(moneyToPut);
        RuntimeException e = assertThrows(RuntimeException.class, () -> atm.receiveMoney(moneyToPut));
        assertThat(e.getMessage()).isEqualTo(moneyNotReceivedErrorMessage);
    }

    @Test
    @DisplayName("Can't get money of wrong nominal from ATM")
    void notDispenseWrongNominalMoneyTest() {
        RuntimeException e = assertThrows(RuntimeException.class, () -> atm.dispenseMoney(5));
        assertThat(e.getMessage()).isEqualTo(moneyNotDispensedErrorMessage);
    }

    @Test
    @DisplayName("Can get money from ATM")
    void dispenseMoneyTest() {
        atm.dispenseMoney(50);

        assertThat(dispenserCartridges.get(NominalValue.FIFTY).getBanknotesQuantity())
                .isEqualTo(dispenserCartridgeQuantity - 1);
    }

    @Test
    @DisplayName("Can't get to much money from ATM")
    void notDispenseToMuchMoneyTest() {
        atm.dispenseMoney(50);
        RuntimeException e = assertThrows(RuntimeException.class, () -> atm.dispenseMoney(5));
        assertThat(e.getMessage()).isEqualTo(moneyNotDispensedErrorMessage);
    }

    @Test
    @DisplayName("Dispensing money reduces rest in the ATM")
    void dispenseMoneyReducesRestTest() {
        atm.dispenseMoney(1150);
        assertThat(atm.getMoneyRest()).isEqualTo(5500);
    }
}
