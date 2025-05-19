package ru.otus.processor;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

@SuppressWarnings({"java:S1135", "java:S125"})
class ProcessorThrowExceptionInEvenSecondTest {
    String exceptionMessage = "It's even second, come back one second later";

    @Test
    void exceptionTest() {
        // given

        LocalDateTime dateTime = LocalDateTime.of(1, 1, 1, 1, 1, 2);

        var dateTimeProvider = mock(DateTimeProvider.class);
        when(dateTimeProvider.getDate()).thenReturn(dateTime);

        var processor = new ProcessorThrowExceptionInEvenSecond(dateTimeProvider);

        var id = 100L;
        var data = "33";
        var field13 = new ObjectForMessage();
        var field13Data = new ArrayList<String>();
        field13Data.add(data);
        field13.setData(field13Data);

        var message =
                new Message.Builder(id).field10("field10").field13(field13).build();

        // when
        EvenSecondException e = assertThrows(EvenSecondException.class, () -> processor.process(message));
        // then
        Assertions.assertThat(e.getMessage()).isEqualTo(exceptionMessage);
    }
}
