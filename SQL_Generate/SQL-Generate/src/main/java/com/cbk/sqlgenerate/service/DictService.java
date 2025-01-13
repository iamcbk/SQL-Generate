package com.cbk.sqlgenerate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cbk.sqlgenerate.model.entity.Dict;

public interface DictService extends IService<Dict> {

    void validAndHandleDict(Dict dict, boolean add);
}
