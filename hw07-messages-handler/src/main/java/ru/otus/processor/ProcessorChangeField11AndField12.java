package ru.otus.processor;

import ru.otus.model.Message;

public class ProcessorChangeField11AndField12 implements Processor {

    @Override
    public Message process(Message message) {
        String temp = message.getField11();
        return message.toBuilder().field11(message.getField12()).field12(temp).build();
    }
}
