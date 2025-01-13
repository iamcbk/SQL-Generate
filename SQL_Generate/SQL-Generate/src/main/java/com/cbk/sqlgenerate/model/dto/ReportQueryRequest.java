package com.cbk.sqlgenerate.model.dto;

import com.cbk.sqlgenerate.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReportQueryRequest extends PageRequest implements Serializable {

    private String content;

    private Integer type;

    private Long reportedId;

    private Long reportedUserId;

    private Integer status;

    private Long userId;

    private static final long serialVersionUID = 1L;
}