package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final String selectAllSqlString;
    private final String selectByIdSqlString;
    private final String insertSqlString;
    private final String updateSqlString;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> metaData) {
        selectAllSqlString = "select * from " + metaData.getName();
        selectByIdSqlString = "select "
                + metaData.getAllFields().stream().map(Field::getName).collect(Collectors.joining(", ")) + " from "
                + metaData.getName() + " where " + metaData.getIdField().getName() + " = ?";
        StringBuilder sb = new StringBuilder();
        insertSqlString = sb.append("insert into ")
                .append(metaData.getName())
                .append("(")
                .append(metaData.getFieldsWithoutId().stream()
                        .map(Field::getName)
                        .collect(Collectors.joining(", ")))
                .append(") values (")
                .append("?,".repeat(metaData.getFieldsWithoutId().size()))
                .deleteCharAt(sb.length() - 1)
                .append(")")
                .toString();
        updateSqlString = "update " + metaData.getName() + " set "
                + metaData.getFieldsWithoutId().stream().map(Field::getName).collect(Collectors.joining(" = ?, "))
                + " = ? where " + metaData.getIdField().getName() + " = ?";
    }

    @Override
    public String getSelectAllSql() {
        return selectAllSqlString;
    }

    @Override
    public String getSelectByIdSql() {
        return selectByIdSqlString;
    }

    @Override
    public String getInsertSql() {
        return insertSqlString;
    }

    @Override
    public String getUpdateSql() {
        return updateSqlString;
    }
}
