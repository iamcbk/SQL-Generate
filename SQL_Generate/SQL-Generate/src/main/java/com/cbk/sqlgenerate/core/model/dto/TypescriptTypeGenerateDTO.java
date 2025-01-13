package com.cbk.sqlgenerate.core.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class TypescriptTypeGenerateDTO {

    private String className;

    private String classComment;

    private List<FieldDTO> fieldList;

    @Data
    public static class FieldDTO {

        private String fieldName;

        private String typescriptType;

        private String comment;
    }

}
