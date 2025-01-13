package com.cbk.sqlgenerate.core.schema;

import lombok.Data;

import java.util.List;

@Data
public class TableSchema {

    private String dbName;

    private String tableName;

    private String tableComment;

    private Integer mockNum;

    private List<Field> fieldList;

    @Data
    public static class Field {

        private String fieldName;

        private String fieldType;

        private String defaultValue;

        private boolean notNull;

        private String comment;

        private boolean primaryKey;

        private boolean autoIncrement;

        private String mockType;

        private String mockParams;

        private String onUpdate;
    }

}
