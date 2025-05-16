package ru.otus;

public class InitialClassImpl implements InitialClassInterface {

    public void calculation(int param1) {
        // Noncompliant - method is empty
    }

    @Log
    public void calculation(int param1, int param2) {
        // Noncompliant - method is empty
    }

    @Log
    public void calculation(int param1, int param2, String param3) {
        // Noncompliant - method is empty
    }
}
