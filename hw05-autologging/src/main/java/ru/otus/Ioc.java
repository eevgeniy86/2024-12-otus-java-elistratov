package ru.otus;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ioc {

    private Ioc() {}

    static InitialClassInterface createMyClass() {
        InvocationHandler handler = new InitialClassInvocationHandler(new InitialClassImpl());
        return (InitialClassInterface) Proxy.newProxyInstance(
                Ioc.class.getClassLoader(), new Class<?>[] {InitialClassInterface.class}, handler);
    }

    static class InitialClassInvocationHandler implements InvocationHandler {
        private static final Logger logger = LoggerFactory.getLogger(InitialClassInvocationHandler.class);
        private final Object myClass;
        private final Set<String> loggedMethods;

        InitialClassInvocationHandler(Object myClass) {
            this.myClass = myClass;

            List<String> loggedMethodsList = Arrays.stream(myClass.getClass().getDeclaredMethods())
                    .filter(m -> {
                        for (Annotation a : m.getDeclaredAnnotations()) {
                            if (a.annotationType().equals(Log.class)) return true;
                        }
                        return false;
                    })
                    .map(this::getMethodSignature)
                    .toList();

            this.loggedMethods = new HashSet<>(loggedMethodsList);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (loggedMethods.contains(getMethodSignature(method))) {
                logger.atInfo()
                        .setMessage("executed method: {}, params: {}")
                        .addArgument(method.getName())
                        .addArgument(Arrays.toString(args))
                        .log();
            }
            return method.invoke(myClass, args);
        }

        private String getMethodSignature(Method method) {
            return method.getName() + Arrays.toString(method.getParameterTypes());
        }
    }
}
