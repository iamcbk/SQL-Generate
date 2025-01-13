package com.cbk.sqlgenerate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cbk.sqlgenerate.model.entity.Report;

public interface ReportService extends IService<Report> {

    void validReport(Report report, boolean add);
}
