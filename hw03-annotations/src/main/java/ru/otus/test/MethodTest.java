package ru.otus.test;

import java.lang.reflect.Method;

class MethodTest {
    private Method method;
    private boolean result;

    MethodTest(Method method) {
        this.method = method;
        this.result = false;
    }

    Method getMethod() {
        return this.method;
    }

    boolean getResult() {
        return result;
    }

    void setResult(boolean result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "test " + method.getName() + " result is " + result;
    }
}
