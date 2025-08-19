package ru.otus.protobuf.server.service;

public class SequenceGeneratorImpl implements SequenceGenerator {
    private final int lastValue;
    private int previous;

    public SequenceGeneratorImpl(int firstValue, int lastValue) {
        this.lastValue = lastValue;
        this.previous = firstValue;
    }

    public boolean hasNext() {
        return previous < lastValue;
    }

    public int getNext() {

        if (previous < lastValue) {
            previous++;
            return previous;
        } else {
            throw new OutOfSequenceException("Next value is out of sequence bounds");
        }
    }
}
