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
        private final InitialClassInterface myClass;
        private final Set<String> loggedMethods;

        InitialClassInvocationHandler(InitialClassInterface myClass) {
            this.myClass = myClass;

            List<String> loggedMethodsList = Arrays.stream(myClass.getClass().getDeclaredMethods())
                    .filter(m -> {
                        for (Annotation a : m.getDeclaredAnnotations()) {
                            if (a.annotationType().equals(Log.class)) return true;
                        }
                        return false;
                    })
                    .map(m -> m.getName() + Arrays.toString(m.getParameterTypes()))
                    .toList();

            this.loggedMethods = new HashSet<>(loggedMethodsList);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (loggedMethods.contains(method.getName() + Arrays.toString(method.getParameterTypes()))) {
                logger.atInfo()
                        .setMessage("executed method: {}, params: {}")
                        .addArgument(method.getName())
                        .addArgument(Arrays.toString(args))
                        .log();
            }
            return method.invoke(myClass, args);
        }
    }
}
