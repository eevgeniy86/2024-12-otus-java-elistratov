package ru.otus.model.service;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.model.domain.Client;
import ru.otus.model.repository.ClientRepository;

@Slf4j
@Service
public class DbServiceClientImpl implements DBServiceClient {
    private final ClientRepository clientRepository;

    public DbServiceClientImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public Client saveClient(Client client) {
        var clientSaved = clientRepository.save(client);
        log.info("saved client: {}", clientSaved);
        return clientSaved;
    }

    @Override
    public Optional<Client> getClient(long id) {
        var clientOptional = clientRepository.findById(id);
        log.info("client: {}", clientOptional);
        return clientOptional;
    }

    @Override
    public List<Client> findAll() {
        var clientList = clientRepository.findByOrderById();
        log.info("clientList:{}", clientList);
        return clientList;
    }

    public List<Client> findByName(String name) {
        var clientList = clientRepository.findByName(name);
        log.info("clientByNameList: {}", clientList);
        return clientList;
    }
}
