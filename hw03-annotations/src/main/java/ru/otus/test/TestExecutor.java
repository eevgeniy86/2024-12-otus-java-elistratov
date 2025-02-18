package ru.otus.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestExecutor {
    public static void main(String[] args) {
        Class<?> clazz = CalculatorServiceImplTest.class;
        execute(clazz);
    }

    public static void execute(Class<?> clazz) {
        List<Method> beforeMethods = new ArrayList<>();
        List<Method> afterMethods = new ArrayList<>();
        List<MethodTest> testsOfMethods = new ArrayList<>();

        prepareMethodLists(clazz, beforeMethods, afterMethods, testsOfMethods);

        for (MethodTest methodTest : testsOfMethods) {
            executeTest(clazz, beforeMethods, afterMethods, methodTest);
        }

        showResults(testsOfMethods);
    }

    private static void prepareMethodLists(
            Class<?> clazz, List<Method> beforeMethods, List<Method> afterMethods, List<MethodTest> testsOfMethods) {
        Method[] allMethods = clazz.getDeclaredMethods();

        for (Method method : allMethods) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation a : annotations) {
                if (a.annotationType().equals(Before.class)) {
                    beforeMethods.add(method);
                }
                if (a.annotationType().equals(After.class)) {
                    afterMethods.add(method);
                }
                if (a.annotationType().equals(Test.class)) {
                    testsOfMethods.add(new MethodTest(method));
                }
            }
        }
    }

    private static void showResults(List<MethodTest> testResults) {
        int positive = 0;
        for (MethodTest methodTest : testResults) {
            System.out.println(methodTest.toString());
            if (methodTest.getResult()) {
                positive++;
            }
        }
        System.out.println("Total number of tests: " + testResults.size() + ", positive: " + positive);
    }

    private static void executeTest(
            Class<?> clazz, List<Method> beforeMethods, List<Method> afterMethods, MethodTest test) {
        try {
            Object testObject = createNewTestClassObject(clazz);
            try {
                invokeBunchOfMethods(testObject, beforeMethods);
                invokeMethod(testObject, test.getMethod());
                test.setResult(true);
            } catch (Exception e) {
                test.setResult(false);
            }
            invokeBunchOfMethods(testObject, afterMethods);

        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private static void invokeBunchOfMethods(Object testObject, List<Method> methods) throws Exception {

        for (Method method : methods) {
            method.setAccessible(true);
            method.invoke(testObject);
        }
    }

    private static void invokeMethod(Object testObject, Method testMethod) throws Exception {
        testMethod.setAccessible(true);
        testMethod.invoke(testObject);
    }

    private static <T> T createNewTestClassObject(Class<T> clazz, Object... args) throws Exception {

        if (args.length == 0) {
            return clazz.getDeclaredConstructor().newInstance();
        } else {
            Class<?>[] classes = toClasses(args);
            return clazz.getDeclaredConstructor(classes).newInstance(args);
        }
    }

    private static Class<?>[] toClasses(Object[] args) {
        return Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
    }
}
