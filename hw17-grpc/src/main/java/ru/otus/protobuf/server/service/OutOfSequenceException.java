package ru.otus.protobuf.server.service;

public class OutOfSequenceException extends RuntimeException {

    public OutOfSequenceException(String message) {
        super(message);
    }
}
