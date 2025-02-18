package ru.otus.test;

import java.lang.reflect.Method;

class MethodTest {
    private Method method;
    private Exception result;

    MethodTest(Method method) {
        this.method = method;
        this.result = null;
    }

    Method getMethod() {
        return this.method;
    }

    Exception getResult() {
        return result;
    }

    void setResult(Exception result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "test " + method.getName() + " result is " + result;
    }
}
