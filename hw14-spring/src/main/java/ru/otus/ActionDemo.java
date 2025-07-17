package ru.otus;

import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.model.domain.Address;
import ru.otus.model.domain.Client;
import ru.otus.model.domain.Phone;
import ru.otus.model.service.DBServiceClient;

@Component("actionDemo")
public class ActionDemo implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(ActionDemo.class);

    private final DBServiceClient dbServiceClient;

    public ActionDemo(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    public void run(String... args) {

        // простой клиент
        dbServiceClient.saveClient(new Client(null, "dbServiceFirst", null, null));

        // клиент со всеми полями
        Address addressForPhoned = new Address(null, "Ленина");
        Set<Phone> phones = Set.of(new Phone(null, "12345"));
        Client clientPhoned = new Client(null, "dbServicePhoned", addressForPhoned, phones);
        Client clientPhonedSaved = dbServiceClient.saveClient(clientPhoned);

        // получаем всех клиентов
        log.info(">>> All clients");
        dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));

        // получаем клиента
        var clientSecondSelected = dbServiceClient
                .getClient(clientPhonedSaved.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientPhoned.getId()));
        log.info(">>> clientPhonedSelected:{}", clientSecondSelected);

        // обновляем клиента
        dbServiceClient.saveClient(new Client(
                clientSecondSelected.getId(),
                "dbServicePhonedUpdated",
                new Address(null, "UpdatedStr"),
                Set.of(new Phone(null, "54321"))));
        var clientUpdated = dbServiceClient
                .getClient(clientSecondSelected.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecondSelected.getId()));
        log.info("clientUpdated:{}", clientUpdated);
    }
}
