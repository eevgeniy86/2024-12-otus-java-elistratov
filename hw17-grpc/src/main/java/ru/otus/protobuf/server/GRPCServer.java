package ru.otus.protobuf.server;

import io.grpc.ServerBuilder;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.server.service.RemoteSequenceService;

@SuppressWarnings({"squid:S106"})
public class GRPCServer {

    private static final Logger logger = LoggerFactory.getLogger(GRPCServer.class);

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {

        var remoteSequenceService = new RemoteSequenceService();

        var server = ServerBuilder.forPort(SERVER_PORT)
                .addService(remoteSequenceService)
                .build();
        server.start();
        logger.atInfo()
                .setMessage("server waiting for client connections on port: {}")
                .addArgument(SERVER_PORT)
                .log();
        server.awaitTermination();
    }
}
