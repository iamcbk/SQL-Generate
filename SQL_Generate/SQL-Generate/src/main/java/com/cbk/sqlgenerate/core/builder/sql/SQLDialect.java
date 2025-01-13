package com.cbk.sqlgenerate.core.builder.sql;

public interface SQLDialect {

    String wrapFieldName(String name);

    String parseFieldName(String fieldName);

    String wrapTableName(String name);

    String parseTableName(String tableName);
}
