package ru.otus.calculator;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamsIOService implements IOService {
    private static final Logger logger = LoggerFactory.getLogger(StreamsIOService.class);
    private final PrintStream out;
    private final Scanner sc;

    public StreamsIOService(PrintStream out, InputStream in) {
        this.out = out;
        this.sc = new Scanner(in);
    }

    @Override
    public void out(String message) {
        out.println(message);
        logger.info("Message printed:{}", message);
    }

    @Override
    public String readString() {
        logger.info("Waiting for string entered");
        return sc.nextLine();
    }
}
