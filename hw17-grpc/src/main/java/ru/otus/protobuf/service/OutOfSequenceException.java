package ru.otus.protobuf.service;

public class OutOfSequenceException extends RuntimeException {

    public OutOfSequenceException(String message) {
        super(message);
    }
}
