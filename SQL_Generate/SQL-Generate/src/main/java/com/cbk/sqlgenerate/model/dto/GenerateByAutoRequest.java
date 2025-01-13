package com.cbk.sqlgenerate.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GenerateByAutoRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    private String content;
}
