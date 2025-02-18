package ru.otus.test;

import org.assertj.core.api.Assertions;
import org.mockito.Mockito;
import ru.otus.calculator.CalculatorService;
import ru.otus.calculator.CalculatorServiceImpl;
import ru.otus.calculator.IOService;
import ru.otus.calculator.StreamsIOService;

public class CalculatorServiceImplTest {
    private IOService ioService;
    private CalculatorService calculator;

    @Before
    void setUp() {
        ioService = Mockito.mock(StreamsIOService.class);
        calculator = new CalculatorServiceImpl(ioService);
    }

    @After
    void finish() {}

    @Test
    void shouldReturnCorrectAnswerToMainQuestion() {
        String expected = "<<42>>";
        calculator.longCalculations();
        Mockito.verify(ioService).out(Mockito.eq(expected));
    }

    @Test
    void shouldReadTwoStrings() {
        Mockito.when(ioService.readString()).thenReturn("1");
        calculator.readTwoDigitsAndMultiply();
        Mockito.verify(ioService, Mockito.times(2)).readString();
    }

    @Test
    void shouldMultiplyNumbers() {
        String n1 = "13";
        String n2 = "7";
        String expected = String.format(n1 + " * " + n2 + " = " + 7 * 13);
        Mockito.when(ioService.readString()).thenReturn(n1).thenReturn(n2);
        calculator.readTwoDigitsAndMultiply();
        Mockito.verify(ioService).out(Mockito.eq(expected));
    }

    @Test
    void tryToMultiplyNaNs() {
        String n1 = "13";
        String n2 = "abc";
        Mockito.when(ioService.readString()).thenReturn(n1).thenReturn(n2);
        calculator.readTwoDigitsAndMultiply();
    }

    @Test
    void shouldThrowExpectedExceptionOnNaN() {
        String n1 = "13";
        String n2 = "abc";
        Mockito.when(ioService.readString()).thenReturn(n1).thenReturn(n2);
        Assertions.assertThatThrownBy(() -> calculator.readTwoDigitsAndMultiply())
                .isInstanceOf(NumberFormatException.class);
    }
}
