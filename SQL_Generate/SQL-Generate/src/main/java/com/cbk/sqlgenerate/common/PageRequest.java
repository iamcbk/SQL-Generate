package com.cbk.sqlgenerate.common;

import com.cbk.sqlgenerate.constant.CommonConstant;
import lombok.Data;

@Data
public class PageRequest {

    private long current = 1;

    private long pageSize = 10;

    private String sortField;

    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}
