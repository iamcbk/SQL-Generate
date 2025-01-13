package com.cbk.sqlgenerate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cbk.sqlgenerate.model.entity.FieldInfo;

public interface FieldInfoService extends IService<FieldInfo> {

    void validAndHandleFieldInfo(FieldInfo fieldInfo, boolean add);
}
