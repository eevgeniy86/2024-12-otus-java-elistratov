package ru.otus.protobuf.client.grpcclient;

import java.util.Deque;
import java.util.Iterator;

public interface GRPCClient {
    Iterator<Integer> getSequenceSync(int firstValue, int lastValue);

    void getSequenceAsync(int firstValue, int lastValue, Deque<Integer> writeTo) throws InterruptedException;
}
