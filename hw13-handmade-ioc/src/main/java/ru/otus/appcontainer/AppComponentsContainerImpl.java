package ru.otus.appcontainer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

@SuppressWarnings({"squid:S1068", "java:S3011"})
public final class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(String packageString) {
        var urls = new ArrayList<>(ClasspathHelper.forJavaClassPath());
        urls.addAll(ClasspathHelper.forPackage(packageString));

        var confBuilder = new ConfigurationBuilder()
                .setScanners(Scanners.SubTypes.filterResultsBy(c -> true))
                .setUrls(urls)
                .filterInputsBy(new FilterBuilder().includePackage(packageString));
        Reflections reflections = new Reflections(confBuilder);
        reflections.getSubTypesOf(Object.class).stream()
                .filter(c -> c.isAnnotationPresent(AppComponentsContainerConfig.class))
                .sorted(Comparator.comparingInt(
                        c -> c.getAnnotation(AppComponentsContainerConfig.class).order()))
                .forEach(this::processConfig);
    }

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        checkConfigClass(initialConfigClass);
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        Arrays.stream(initialConfigClasses)
                .sorted(Comparator.comparingInt(
                        c -> c.getAnnotation(AppComponentsContainerConfig.class).order()))
                .forEach(c -> {
                    checkConfigClass(c);
                    processConfig(c);
                });
    }

    private void processConfig(Class<?> configClass) {
        Object config;
        try {
            Constructor<?> configConstructor = configClass.getConstructor();
            configConstructor.setAccessible(true);
            config = configConstructor.newInstance();
        } catch (InvocationTargetException
                | NoSuchMethodException
                | InstantiationException
                | IllegalAccessException e) {
            throw new IncorrectConfigException(e);
        }
        List<Method> methods = getMethodsOfConfigClass(configClass);
        initAppComponents(methods, config);
    }

    private static List<Method> getMethodsOfConfigClass(Class<?> configClass) {
        List<Method> methods = new ArrayList<>();
        for (Method m : configClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(AppComponent.class)) {
                methods.add(m);
            }
        }
        return methods;
    }

    private void initAppComponents(List<Method> methods, Object config) {
        methods.stream()
                .sorted(Comparator.comparingInt(
                        m -> m.getAnnotation(AppComponent.class).order()))
                .forEach(m -> {
                    if (!appComponentsByName.containsKey(
                            m.getAnnotation(AppComponent.class).name())) {
                        Object obj = invokeConfigMethod(config, m);
                        appComponents.add(obj);
                        appComponentsByName.put(
                                m.getAnnotation(AppComponent.class).name(), obj);
                    } else {
                        throw new IncorrectConfigException(String.format(
                                "Config class %s has duplicated component or component name for method %s",
                                m.getDeclaringClass().getName(), m.getName()));
                    }
                });
    }

    private Object invokeConfigMethod(Object config, Method method) {
        method.setAccessible(true);
        List<Parameter> params = List.of(method.getParameters());
        List<Object> paramObjects = new ArrayList<>();
        for (Parameter param : params) {
            paramObjects.add(getAppComponent(param.getType()));
        }
        try {
            return method.invoke(config, paramObjects.toArray());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IncorrectConfigException(e);
        }
    }

    private static void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        C result = null;
        for (Object component : appComponents) {
            if (componentClass.isInstance(component)) {
                if (result != null) {
                    throw new IncorrectConfigException("Too many elements of the same type");
                }
                result = componentClass.cast(component);
            }
        }
        if (result != null) {
            return result;
        }
        throw new IncorrectConfigException("No such element");
    }

    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        var res = appComponentsByName.get(componentName);
        if (res == null) {
            throw new IncorrectConfigException("No such element");
        }
        return (C) res;
    }
}
