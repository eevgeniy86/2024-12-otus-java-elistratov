package ru.otus.protobuf.client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.client.grpcclient.GRPCClientImpl;

public class Calculator {
    private static final Logger logger = LoggerFactory.getLogger(Calculator.class);
    private static final AtomicInteger serverValue = new AtomicInteger(0);

    private static final int SERVER_FIRST_VALUE = 0;
    private static final int SERVER_LAST_VALUE = 30;

    public static void main(String[] args) throws InterruptedException {
        var latch = new CountDownLatch(2);
        new Thread(() -> {
                    try (GRPCClientImpl grpcClient = new GRPCClientImpl()) {
                        latch.countDown();
                        latch.await();
                        grpcClient.getSequenceAsync(SERVER_FIRST_VALUE, SERVER_LAST_VALUE, serverValue);
                    } catch (InterruptedException e) {
                        logger.atError()
                                .setMessage("Thread of server call interrupted")
                                .log();
                        Thread.currentThread().interrupt();
                    }
                })
                .start();
        latch.countDown();
        latch.await();
        calculateResults();
    }

    private static void calculateResults() throws InterruptedException {
        int currentValue = 0;
        for (int i = 0; i <= 50; i++) {
            Thread.sleep(1000);
            int seqValue = serverValue.getAndSet(0);
            currentValue = currentValue + seqValue + 1;
            logger.atInfo()
                    .setMessage("Current calculated value: {}")
                    .addArgument(currentValue)
                    .log();
        }
    }
}
