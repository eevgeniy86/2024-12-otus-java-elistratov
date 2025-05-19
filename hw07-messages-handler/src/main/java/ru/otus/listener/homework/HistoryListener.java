package ru.otus.listener.homework;

import java.util.*;
import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

public class HistoryListener implements Listener, HistoryReader {
    private final Set<Message> messages;

    public HistoryListener() {
        this.messages = new HashSet<>();
    }

    @Override
    public void onUpdated(Message msg) {

        messages.remove(msg);

        ObjectForMessage obj = new ObjectForMessage();
        if (msg.getField13().getData() != null) {
            List<String> temp = new ArrayList<>(msg.getField13().getData());
            obj.setData(temp);
        }
        Message msn = msg.toBuilder().field13(obj).build();
        messages.add(msn);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return messages.stream().filter(a -> a.getId() == id).findAny();
    }
}
