package ru.otus.calculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static final StreamsIOService io = new StreamsIOService(System.out, System.in);
    public static final CalculatorService calc = new CalculatorServiceImpl(io);
    public static final String MULTIPLIER_DESC = "Add two numbers:";

    public static void main(String[] args) {

        logger.info("App execution started");

        calc.readTwoDigitsAndMultiply();

        calc.readTwoDigitsAndMultiply(MULTIPLIER_DESC);

        calc.longCalculations();

        logger.info("App execution finished");
    }
}
