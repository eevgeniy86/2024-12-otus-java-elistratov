package ru.otus.protobuf.client.grpcclient;

import com.google.common.collect.Iterators;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proto.v1.GetSequenceRequest;
import proto.v1.GetSequenceResponse;
import proto.v1.SequenceServiceGrpc;

@SuppressWarnings({"squid:S106", "squid:S2142"})
public class GRPCClientImpl implements GRPCClient, AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(GRPCClientImpl.class);
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private final ManagedChannel channel;

    public GRPCClientImpl() {
        channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();
        logger.atInfo().setMessage("Client is ready").log();
    }

    public Iterator<Integer> getSequenceSync(int firstValue, int lastValue) {
        GetSequenceRequest getSequenceRequest = createGetSequenceRequest(firstValue, lastValue);

        var stub = SequenceServiceGrpc.newBlockingStub(channel);
        var responseIterator = stub.getSequence(getSequenceRequest);
        logger.atInfo().setMessage("Got sequence from server sync").log();
        return Iterators.transform(responseIterator, GetSequenceResponse::getValue);
    }

    public void getSequenceAsync(int firstValue, int lastValue, Collection<Integer> writeTo)
            throws InterruptedException {
        GetSequenceRequest getSequenceRequest = createGetSequenceRequest(firstValue, lastValue);
        var latch = new CountDownLatch(1);
        var stub = SequenceServiceGrpc.newStub(channel);
        stub.getSequence(getSequenceRequest, new StreamObserver<>() {
            @Override
            public void onNext(GetSequenceResponse sequenceResponse) {
                writeTo.add(sequenceResponse.getValue());
                logger.atInfo()
                        .setMessage("New value got from server async: {}")
                        .addArgument(sequenceResponse.getValue())
                        .log();
            }

            @Override
            public void onError(Throwable t) {
                logger.atError()
                        .setMessage("Error during getting sequence from server")
                        .log();
            }

            @Override
            public void onCompleted() {
                logger.atInfo().setMessage("Sequence transfer finished").log();
                latch.countDown();
            }
        });
        latch.await();
    }

    public void close() {
        channel.shutdown();
        logger.atInfo().setMessage("Client stopped").log();
    }

    private GetSequenceRequest createGetSequenceRequest(int firstValue, int lastValue) {
        return GetSequenceRequest.newBuilder()
                .setFirstValue(firstValue)
                .setLastValue(lastValue)
                .build();
    }
}
