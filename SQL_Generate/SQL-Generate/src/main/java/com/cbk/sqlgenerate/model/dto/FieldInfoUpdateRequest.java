package com.cbk.sqlgenerate.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class FieldInfoUpdateRequest implements Serializable {

    private long id;

    private String name;

    private String fieldName;

    private String content;

    private Integer reviewStatus;

    private String reviewMessage;

    private static final long serialVersionUID = 1L;
}