package ru.otus;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.*;

public class HomeWork {
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    /*
    Реализовать to do:
      1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage) - DONE
      2. Сделать процессор, который поменяет местами значения field11 и field12 - DONE
      3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом) - DONE
            Секунда должна определяьться во время выполнения. - DONE
            Тест - важная часть задания - DONE
            Обязательно посмотрите пример к паттерну Мементо! - DONE
      4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились) - DONE
         Уже есть заготовка - класс HistoryListener, надо сделать его реализацию - DONE
         Для него уже есть тест, убедитесь, что тест проходит - DONE
    */

    public static void main(String[] args) {

        /*
          по аналогии с Demo.class
          из элеменов "to do" создать new ComplexProcessor и обработать сообщение - DONE
        */

        List<String> list = List.of("1", "2", "3");
        ObjectForMessage obj = new ObjectForMessage();
        obj.setData(list);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field11("field11")
                .field12("field12")
                .field13(obj)
                .build();

        var processors = List.of(
                new ProcessorChangeField11AndField12(), new ProcessorThrowExceptionInEvenSecond(LocalDateTime::now));
        var complexProcessor = new ComplexProcessor(processors, ex -> {});

        var listener = new HistoryListener();
        complexProcessor.addListener(listener);
        var result = complexProcessor.handle(message);
        logger.info("result:{}", result);

        var found = listener.findMessageById(1L);
        found.ifPresent(value -> logger.info("message by id:{}", value));

        complexProcessor.removeListener(listener);
    }
}
