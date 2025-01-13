package com.cbk.sqlgenerate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cbk.sqlgenerate.common.ErrorCode;
import com.cbk.sqlgenerate.core.GeneratorFacade;
import com.cbk.sqlgenerate.core.schema.TableSchema;
import com.cbk.sqlgenerate.exception.BusinessException;
import com.cbk.sqlgenerate.mapper.FieldInfoMapper;
import com.cbk.sqlgenerate.model.entity.FieldInfo;
import com.cbk.sqlgenerate.model.enums.ReviewStatusEnum;
import com.cbk.sqlgenerate.service.FieldInfoService;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class FieldServiceImpl extends ServiceImpl<FieldInfoMapper, FieldInfo> implements FieldInfoService {

    private final static Gson GSON = new Gson();

    @Override
    public void validAndHandleFieldInfo(FieldInfo fieldInfo, boolean add) {
        if (fieldInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String content = fieldInfo.getContent();
        String name = fieldInfo.getName();
        Integer reviewStatus = fieldInfo.getReviewStatus();
        // 创建时，所有参数必须非空
        if (add && StringUtils.isAnyBlank(name, content)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (StringUtils.isNotBlank(name) && name.length() > 30) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
        if (StringUtils.isNotBlank(content)) {
            if (content.length() > 20000) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
            }
            // 校验字段内容
            try {
                TableSchema.Field field = GSON.fromJson(content, TableSchema.Field.class);
                GeneratorFacade.validField(field);
                // 填充 fieldName
                fieldInfo.setFieldName(field.getFieldName());
            } catch (Exception e) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容格式错误");
            }
        }
        if (reviewStatus != null && !ReviewStatusEnum.getValues().contains(reviewStatus)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    }
}




