package com.cbk.sqlgenerate.model.dto;

import com.cbk.sqlgenerate.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class FieldInfoQueryRequest extends PageRequest implements Serializable {

    private String searchName;

    private String name;

    private String fieldName;

    private String content;

    private Integer reviewStatus;

    private Long userId;

    private static final long serialVersionUID = 1L;
}