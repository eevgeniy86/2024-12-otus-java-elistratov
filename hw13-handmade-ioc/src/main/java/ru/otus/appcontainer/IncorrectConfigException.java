package ru.otus.appcontainer;

public class IncorrectConfigException extends RuntimeException {
    public IncorrectConfigException(String s) {
        super(s);
    }

    public IncorrectConfigException(Exception e) {
        super(e);
    }
}
