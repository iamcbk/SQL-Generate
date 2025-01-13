package com.cbk.sqlgenerate.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName(value ="report")
@Data
public class Report implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String content;

    private Integer type;

    private Long reportedId;

    private Long reportedUserId;

    private Integer status;

    private Long userId;

    private Date createTime;

    private Date updateTime;

    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}