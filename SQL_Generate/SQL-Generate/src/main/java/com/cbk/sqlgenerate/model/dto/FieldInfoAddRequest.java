package com.cbk.sqlgenerate.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class FieldInfoAddRequest implements Serializable {

    private String name;

    private String content;

    private static final long serialVersionUID = 1L;
}