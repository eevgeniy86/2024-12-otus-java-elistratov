package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("java:S112")
public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final String classSimpleName;
    private final Constructor<T> constructor;
    private final Field idField;
    private final List<Field> allFields;
    private final List<Field> withoutIdFields;

    public EntityClassMetaDataImpl(Class<T> clazz) {

        classSimpleName = clazz.getSimpleName();
        try {
            constructor = clazz.getConstructor();
        } catch (NoSuchMethodException nsme) {
            throw new RuntimeException(nsme);
        }
        Field idf = null;
        for (Field f : clazz.getDeclaredFields()) {
            if (Arrays.stream(f.getDeclaredAnnotations())
                    .anyMatch(a -> a.annotationType().equals(Id.class))) {
                idf = f;
            }
        }
        if (idf == null) {
            throw new RuntimeException("Id element not found");
        }
        idField = idf;

        allFields = List.of(clazz.getDeclaredFields());

        withoutIdFields = new ArrayList<>(allFields);
        withoutIdFields.remove(idField);
    }

    @Override
    public String getName() {
        return classSimpleName;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return withoutIdFields;
    }
}
