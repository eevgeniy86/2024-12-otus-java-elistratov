package ru.otus.listener.homework;

import java.util.*;
import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

public class HistoryListener implements Listener, HistoryReader {
    private final Map<Long, Message> messages;

    public HistoryListener() {
        this.messages = new HashMap<>();
    }

    @Override
    public void onUpdated(Message msg) {
        Message.Builder historyBuilder = msg.toBuilder();
        if (msg.getField13() != null) {
            ObjectForMessage obj = new ObjectForMessage();
            if (msg.getField13().getData() != null) {
                List<String> temp = new ArrayList<>(msg.getField13().getData());
                obj.setData(temp);
            }
            historyBuilder.field13(obj);
        }
        messages.put(msg.getId(), historyBuilder.build());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(messages.get(id));
    }
}
