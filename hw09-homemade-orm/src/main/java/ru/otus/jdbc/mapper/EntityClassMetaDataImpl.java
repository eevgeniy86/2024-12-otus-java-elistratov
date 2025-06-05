package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("java:S112")
public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> clazz;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return clazz.getName();
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            return clazz.getConstructor();
        } catch (NoSuchMethodException nsme) {
            throw new RuntimeException(nsme);
        }
    }

    @Override
    public Field getIdField() {
        for (Field f : clazz.getDeclaredFields()) {
            if (Arrays.stream(f.getDeclaredAnnotations())
                    .anyMatch(a -> a.annotationType().equals(Id.class))) {
                return f;
            }
        }
        throw new RuntimeException("Id element not found");
    }

    @Override
    public List<Field> getAllFields() {
        return List.of(clazz.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        List<Field> result = new ArrayList<>(List.of(clazz.getDeclaredFields()));
        result.remove(this.getIdField());
        return result;
    }
}
