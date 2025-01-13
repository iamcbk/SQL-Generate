package com.cbk.sqlgenerate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cbk.sqlgenerate.model.entity.TableInfo;

public interface TableInfoService extends IService<TableInfo> {

    void validAndHandleTableInfo(TableInfo tableInfo, boolean add);
}
