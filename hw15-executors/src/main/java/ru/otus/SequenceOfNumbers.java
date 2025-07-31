package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SequenceOfNumbers {
    private static final Logger logger = LoggerFactory.getLogger(SequenceOfNumbers.class);
    private int last = 0;
    private int increment = 0;
    private int direction = 1;
    private static final int MAX = 10;
    private static final int MIN = 1;

    private synchronized void action(int rise) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // spurious wakeup https://en.wikipedia.org/wiki/Spurious_wakeup
                // поэтому не if
                while (Math.abs(increment) == rise) {
                    this.wait();
                }
                if (last + rise * direction > MAX || last + rise * direction < MIN) {
                    direction = -direction;
                }
                increment = rise * direction;

                last += (increment);

                logger.atInfo().setMessage(String.valueOf(last)).log();

                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        SequenceOfNumbers sequenceofNumbers = new SequenceOfNumbers();

        Thread firstThread = new Thread(() -> sequenceofNumbers.action(1));
        firstThread.setName("First");
        firstThread.start();
        Thread secondThread = new Thread(() -> sequenceofNumbers.action(0));
        secondThread.setName("Second");
        secondThread.start();
    }

    private static void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
