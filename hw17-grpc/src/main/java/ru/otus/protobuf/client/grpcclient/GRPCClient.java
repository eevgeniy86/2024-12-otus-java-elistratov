package ru.otus.protobuf.client.grpcclient;

import java.util.concurrent.atomic.AtomicInteger;

public interface GRPCClient {

    void getSequenceAsync(int firstValue, int lastValue, AtomicInteger writeTo) throws InterruptedException;
}
