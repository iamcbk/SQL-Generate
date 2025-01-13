package com.cbk.sqlgenerate.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@TableName(value ="field_info")
@Data
public class FieldInfo implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String fieldName;

    private String content;

    private Integer reviewStatus;

    private String reviewMessage;

    private Long userId;

    private Date createTime;

    private Date updateTime;

    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}