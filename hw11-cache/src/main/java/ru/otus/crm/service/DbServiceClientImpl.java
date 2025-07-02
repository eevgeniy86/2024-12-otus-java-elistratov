package ru.otus.crm.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Client;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;
    private final HwCache<String, Client> cache;

    public DbServiceClientImpl(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
        this.cache = null;
    }

    public DbServiceClientImpl(
            TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate, HwCache<String, Client> cache) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
        this.cache = cache;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                log.info("created client: {}", createdClient);
                putToCache(createdClient);
                return createdClient;
            }
            dataTemplate.update(connection, client);
            log.info("updated client: {}", client);
            putToCache(client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        var clientCached = getFromCache(id);
        if (clientCached.isPresent()) {
            return clientCached;
        }
        return transactionRunner.doInTransaction(connection -> {
            var clientOptional = dataTemplate.findById(connection, id);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var clientList = dataTemplate.findAll(connection);
            log.info("clientList:{}", clientList);
            for (Client client : clientList) {
                putToCache(client);
            }
            return clientList;
        });
    }

    private void putToCache(Client client) {
        if (this.cache != null) {
            cache.put(getKeyForCache(client), new Client(client.getId(), client.getName()));
        }
    }

    private String getKeyForCache(Client client) {
        return String.valueOf(client.getId());
    }

    private Optional<Client> getFromCache(long id) {
        Client result = null;
        if (this.cache != null) {
            var clientFromCache = cache.get(String.valueOf(id));
            if (clientFromCache != null) {
                result = new Client(clientFromCache.getId(), clientFromCache.getName());
            }
        }
        return Optional.ofNullable(result);
    }
}
