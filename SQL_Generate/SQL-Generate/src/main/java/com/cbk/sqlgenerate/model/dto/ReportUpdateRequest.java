package com.cbk.sqlgenerate.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReportUpdateRequest implements Serializable {

    private Long id;

    private Integer type;

    private Integer status;

    private static final long serialVersionUID = 1L;
}