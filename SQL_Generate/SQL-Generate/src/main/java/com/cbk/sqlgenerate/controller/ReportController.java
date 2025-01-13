package com.cbk.sqlgenerate.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cbk.sqlgenerate.annotation.AuthCheck;
import com.cbk.sqlgenerate.common.BaseResponse;
import com.cbk.sqlgenerate.common.DeleteRequest;
import com.cbk.sqlgenerate.common.ErrorCode;
import com.cbk.sqlgenerate.common.ResultUtils;
import com.cbk.sqlgenerate.constant.CommonConstant;
import com.cbk.sqlgenerate.exception.BusinessException;
import com.cbk.sqlgenerate.model.dto.ReportAddRequest;
import com.cbk.sqlgenerate.model.dto.ReportQueryRequest;
import com.cbk.sqlgenerate.model.dto.ReportUpdateRequest;
import com.cbk.sqlgenerate.model.entity.Dict;
import com.cbk.sqlgenerate.model.entity.Report;
import com.cbk.sqlgenerate.model.entity.User;
import com.cbk.sqlgenerate.model.enums.ReportStatusEnum;
import com.cbk.sqlgenerate.service.DictService;
import com.cbk.sqlgenerate.service.ReportService;
import com.cbk.sqlgenerate.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/report")
@Slf4j
public class ReportController {

    @Resource
    private ReportService reportService;

    @Resource
    private DictService dictService;

    @Resource
    private UserService userService;

    @PostMapping("/add")
    public BaseResponse<Long> addReport(@RequestBody ReportAddRequest reportAddRequest, HttpServletRequest request) {
        if (reportAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Report report = new Report();
        BeanUtils.copyProperties(reportAddRequest, report);
        reportService.validReport(report, true);
        User loginUser = userService.getLoginUser(request);
        Dict dict = dictService.getById(report.getReportedId());
        if (dict == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "举报对象不存在");
        }
        report.setReportedUserId(dict.getUserId());
        report.setUserId(loginUser.getId());
        report.setStatus(ReportStatusEnum.DEFAULT.getValue());
        boolean result = reportService.save(report);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newReportId = report.getId();
        return ResultUtils.success(newReportId);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteReport(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Report oldReport = reportService.getById(id);
        if (oldReport == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldReport.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = reportService.removeById(id);
        return ResultUtils.success(b);
    }

    @PostMapping("/update")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> updateReport(@RequestBody ReportUpdateRequest reportUpdateRequest) {
        if (reportUpdateRequest == null || reportUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Report report = new Report();
        BeanUtils.copyProperties(reportUpdateRequest, report);
        reportService.validReport(report, false);
        long id = reportUpdateRequest.getId();
        // 判断是否存在
        Report oldReport = reportService.getById(id);
        if (oldReport == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        boolean result = reportService.updateById(report);
        return ResultUtils.success(result);
    }

    @GetMapping("/get")
    public BaseResponse<Report> getReportById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Report report = reportService.getById(id);
        return ResultUtils.success(report);
    }

    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<Report>> listReport(ReportQueryRequest reportQueryRequest) {
        Report reportQuery = new Report();
        if (reportQueryRequest != null) {
            BeanUtils.copyProperties(reportQueryRequest, reportQuery);
        }
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>(reportQuery);
        List<Report> reportList = reportService.list(queryWrapper);
        return ResultUtils.success(reportList);
    }

    @GetMapping("/list/page")
    public BaseResponse<Page<Report>> listReportByPage(ReportQueryRequest reportQueryRequest) {
        if (reportQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Report reportQuery = new Report();
        BeanUtils.copyProperties(reportQueryRequest, reportQuery);
        long current = reportQueryRequest.getCurrent();
        long size = reportQueryRequest.getPageSize();
        String sortField = reportQueryRequest.getSortField();
        String sortOrder = reportQueryRequest.getSortOrder();
        String content = reportQuery.getContent();
        // content 需支持模糊搜索
        reportQuery.setContent(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>(reportQuery);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        Page<Report> reportPage = reportService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(reportPage);
    }
}
