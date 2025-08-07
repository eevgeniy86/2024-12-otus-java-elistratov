package ru.otus.protobuf.service;

import java.util.OptionalInt;

public interface SequenceGenerator {
    boolean hasNext();
    int getNext();
}
