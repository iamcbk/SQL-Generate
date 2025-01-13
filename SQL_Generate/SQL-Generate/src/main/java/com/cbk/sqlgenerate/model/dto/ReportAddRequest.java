package com.cbk.sqlgenerate.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReportAddRequest implements Serializable {

    private String content;

    private Integer type;

    private Long reportedId;

    private static final long serialVersionUID = 1L;
}