package ru.otus;

import com.google.common.base.Optional;

@SuppressWarnings("java:S106")
public class HelloOtus {

    public static void main(String[] args) {
        Integer value1 = null;
        Optional<Integer> a = Optional.fromNullable(value1);
        System.out.println(a);
    }
}
