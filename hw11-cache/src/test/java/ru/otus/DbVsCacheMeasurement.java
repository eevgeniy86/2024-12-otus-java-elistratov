package ru.otus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.jdbc.mapper.*;

@State(Scope.Thread)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class DbVsCacheMeasurement {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";
    private static final Logger logger = LoggerFactory.getLogger(DbVsCacheMeasurement.class);
    private static DBServiceClient dbServiceClient;

    public static void main(String[] args) throws RunnerException {
        var opt = new OptionsBuilder()
                .include(DbVsCacheMeasurement.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }

    @Setup
    public void setUp() {
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        dbServiceClient = getDbServiceClient(dataSource);
    }

    @State(Scope.Benchmark)
    public static class StateForCached {
        List<Long> ids = new ArrayList<>();

        @Setup
        public void setUp() {
            for (int i = 1; i <= 10; i++) {
                Client c = new Client("client" + i);
                Client saved = dbServiceClient.saveClient(c);
                ids.add(saved.getId());
            }
        }
    }

    @State(Scope.Benchmark)
    public static class StateForNotCached {
        List<Long> ids = new ArrayList<>();

        @Setup
        public void setUp() {
            for (int i = 1; i <= 10; i++) {
                Client c = new Client("client" + i);
                Client saved = dbServiceClient.saveClient(c);
                ids.add(saved.getId());
            }
            System.gc();
        }
    }

    @Benchmark
    public void cachedMeasure(StateForCached s) {
        for (long id : s.ids) {
            dbServiceClient.getClient(id);
        }
    }

    @Benchmark
    public void NotCachedMeasure(StateForNotCached s) {
        for (long id : s.ids) {
            dbServiceClient.getClient(id);
        }
    }

    private static DbServiceClientImpl getDbServiceClient(DriverManagerDataSource dataSource) {
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();
        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient);

        @SuppressWarnings("java:S1604")
        var listener = new HwListener<String, Client>() {
            @Override
            public void notify(String id, Client client, String action) {
                logger.info("id:{}, client:{}, action: {}", id, client, action);
            }
        };
        HwCache<String, Client> cache = new MyCache<>();
        cache.addListener(listener);
        return new DbServiceClientImpl(transactionRunner, dataTemplateClient, cache);
    }

    private static void flywayMigrations(DataSource dataSource) {
        logger.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        logger.info("db migration finished.");
        logger.info("***");
    }
}
