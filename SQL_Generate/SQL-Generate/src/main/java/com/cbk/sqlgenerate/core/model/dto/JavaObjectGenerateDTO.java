package com.cbk.sqlgenerate.core.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class JavaObjectGenerateDTO {

    private String className;

    private String objectName;

    private List<FieldDTO> fieldList;

    @Data
    public static class FieldDTO {

        private String setMethod;

        private String value;
    }

}
