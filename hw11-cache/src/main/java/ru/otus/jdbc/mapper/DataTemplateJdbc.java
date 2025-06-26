package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@SuppressWarnings({"java:S1068", "java:S112", "java:S3011"})
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> classMetaData;

    public DataTemplateJdbc(
            DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> classMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.classMetaData = classMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {

        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {

                    return constructObject(rs);
                }
                return null;
            } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor
                .executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
                    var objList = new ArrayList<T>();

                    try {

                        while (rs.next()) {
                            var obj = constructObject(rs);
                            objList.add(obj);
                        }
                        return objList;
                    } catch (SQLException
                            | InstantiationException
                            | IllegalAccessException
                            | InvocationTargetException e) {
                        throw new DataTemplateException(e);
                    }
                })
                .orElseThrow(() -> new DataTemplateException(
                        new RuntimeException("Can't find data for class " + classMetaData.getName())));
    }

    @Override
    public long insert(Connection connection, T client) {

        try {
            List<Field> fields = classMetaData.getFieldsWithoutId();
            List<Object> params = fields.stream()
                    .map(f -> {
                        f.setAccessible(true);
                        try {
                            return f.get(client);
                        } catch (IllegalAccessException e) {
                            throw new DataTemplateException(e);
                        }
                    })
                    .toList();
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {

        try {
            List<Field> fields = classMetaData.getFieldsWithoutId();
            List<Object> params = new ArrayList<>(fields.stream()
                    .map(f -> {
                        f.setAccessible(true);
                        try {
                            return f.get(client);
                        } catch (IllegalAccessException e) {
                            throw new DataTemplateException(e);
                        }
                    })
                    .toList());

            params.add(classMetaData.getIdField());
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private T constructObject(ResultSet rs)
            throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var obj = classMetaData.getConstructor().newInstance();
        var fields = classMetaData.getAllFields();
        for (Field f : fields) {
            f.setAccessible(true);
            f.set(obj, rs.getObject(f.getName()));
        }
        return obj;
    }
}
