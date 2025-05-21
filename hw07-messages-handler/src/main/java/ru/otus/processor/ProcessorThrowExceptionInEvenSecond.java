package ru.otus.processor;

import ru.otus.model.Message;

public class ProcessorThrowExceptionInEvenSecond implements Processor {
    private final DateTimeProvider dateTimeProvider;

    public ProcessorThrowExceptionInEvenSecond(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        if (dateTimeProvider.getDate().getSecond() % 2 == 0) {
            throw new EvenSecondException("It's even second, come back one second later");
        }
        return message;
    }
}
