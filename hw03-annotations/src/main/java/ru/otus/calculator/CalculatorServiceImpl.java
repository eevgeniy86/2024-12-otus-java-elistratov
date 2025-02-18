package ru.otus.calculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalculatorServiceImpl implements CalculatorService {
    private static final Logger logger = LoggerFactory.getLogger(CalculatorServiceImpl.class);
    private final IOService ioService;

    public CalculatorServiceImpl(IOService ioService) {
        this.ioService = ioService;
    }

    @Override
    public void readTwoDigitsAndMultiply() {
        int d1;
        int d2;
        try {
            d1 = Integer.parseInt(ioService.readString());
            d2 = Integer.parseInt(ioService.readString());
            logger.info("Entered numbers:{}, {}", d1, d2);
        } catch (NumberFormatException nfe) {
            logger.error("Entered NaN");
            throw nfe;
        }

        multiplyAndOutResult(d1, d2);
    }

    @Override
    public void readTwoDigitsAndMultiply(String prompt) {
        ioService.out(prompt);
        logger.info("Asked for numbers");

        readTwoDigitsAndMultiply();
    }

    @Override
    public void multiplyTwoDigits(String prompt, int d1, int d2) {
        ioService.out(prompt);
        logger.info("Numbers for multiplication:{}, {}", d1, d2);

        multiplyAndOutResult(d1, d2);
    }

    @Override
    public void longCalculations() {
        ioService.out("Ответ на главный вопрос жизни, вселенной и всего такого");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            logger.error("Answer to Life, the Universe, and Everything question never will be given");

            Thread.currentThread().interrupt();
        }
        ioService.out("<<42>>");
        logger.info("Answer to Life, the Universe, and Everything question given");
    }

    private void multiplyAndOutResult(int d1, int d2) {
        ioService.out(String.format("%d * %d = %d", d1, d2, d1 * d2));
        logger.info("Multiplication completed");
    }
}
