package com.cbk.sqlgenerate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cbk.sqlgenerate.common.ErrorCode;
import com.cbk.sqlgenerate.exception.BusinessException;
import com.cbk.sqlgenerate.mapper.ReportMapper;
import com.cbk.sqlgenerate.model.entity.Report;
import com.cbk.sqlgenerate.model.enums.ReportStatusEnum;
import com.cbk.sqlgenerate.service.ReportService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {

    @Override
    public void validReport(Report report, boolean add) {
        if (report == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String content = report.getContent();
        Long reportedId = report.getReportedId();
        Integer status = report.getStatus();
        if (StringUtils.isNotBlank(content) && content.length() > 1024) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
        // 创建时必须指定
        if (add) {
            if (reportedId == null || reportedId <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        } else {
            if (status != null && !ReportStatusEnum.getValues().contains(status)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
    }
}




