package ru.otus.test;

import java.lang.reflect.Method;

class MethodTest {
    private Method method;
    private Exception error;

    MethodTest(Method method) {
        this.method = method;
        this.error = null;
    }

    Method getMethod() {
        return this.method;
    }

    Exception getError() {
        return error;
    }

    void setError(Exception error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "test " + method.getName() + " result is " + error;
    }
}
