package ru.otus.protobuf.service;

public class SequenceGeneratorImpl implements SequenceGenerator {
    private final int lastValue;
    private int next;

    public SequenceGeneratorImpl(int firstValue, int lastValue) {
        this.lastValue = lastValue;
        this.next = firstValue;
    }

    public boolean hasNext() {
        return next <= lastValue;
    }

    public int getNext() {

        if (next <= lastValue) {
            var result = next;
            next++;
            return result;
        } else {
            throw new OutOfSequenceException("Next value is out of sequence bounds");
        }
    }
}
