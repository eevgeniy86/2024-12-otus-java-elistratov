package ru.otus;

public class Main {
    public static void main(String[] args) {
        InitialClassInterface myClass = Ioc.createMyClass();
        myClass.calculation(1);
        myClass.calculation(1, 2);
        myClass.calculation(1, 2, "a");
    }
}
