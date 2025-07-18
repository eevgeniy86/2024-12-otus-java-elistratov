package ru.otus.model.service;

import java.util.List;
import java.util.Optional;
import ru.otus.model.domain.Client;

public interface DBServiceClient {

    Client saveClient(Client client);

    Optional<Client> getClient(long id);

    List<Client> findAll();

    List<Client> findByName(String name);
}
