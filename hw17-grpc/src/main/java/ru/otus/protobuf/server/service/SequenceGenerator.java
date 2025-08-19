package ru.otus.protobuf.server.service;

public interface SequenceGenerator {
    boolean hasNext();

    int getNext();
}
