package ru.otus.jdbc.mapper;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final String selectAllSqlString;
    private final String selectByIdSqlString;
    private final String insertSqlString;
    private final String updateSqlString;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> metaData) {
        if (metaData.getName().equals("ru.otus.crm.model.Client")) {
            selectAllSqlString = "select * from client";
            selectByIdSqlString = "select id, name from client where id  = ?";
            insertSqlString = "insert into client(name) values (?)";
            updateSqlString = "update client set name = ? where id = ?";
        } else if (metaData.getName().equals("ru.otus.crm.model.Manager")) {
            selectAllSqlString = "select * from manager";
            selectByIdSqlString = "select no, label, param1 from manager where no  = ?";
            insertSqlString = "insert into manager(label, param1) values (?, ?)";
            updateSqlString = "update manager set label = ?, param1 = ? where no = ?";
        } else {
            throw new IllegalArgumentException("Unknown datatype object");
        }
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
