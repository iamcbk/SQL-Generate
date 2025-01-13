package com.cbk.sqlgenerate.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cbk.sqlgenerate.annotation.AuthCheck;
import com.cbk.sqlgenerate.common.BaseResponse;
import com.cbk.sqlgenerate.common.DeleteRequest;
import com.cbk.sqlgenerate.common.ErrorCode;
import com.cbk.sqlgenerate.common.ResultUtils;
import com.cbk.sqlgenerate.constant.CommonConstant;
import com.cbk.sqlgenerate.core.builder.SqlBuilder;
import com.cbk.sqlgenerate.core.schema.TableSchema;
import com.cbk.sqlgenerate.exception.BusinessException;
import com.cbk.sqlgenerate.model.dto.TableInfoAddRequest;
import com.cbk.sqlgenerate.model.dto.TableInfoQueryRequest;
import com.cbk.sqlgenerate.model.dto.TableInfoUpdateRequest;
import com.cbk.sqlgenerate.model.entity.TableInfo;
import com.cbk.sqlgenerate.model.entity.User;
import com.cbk.sqlgenerate.model.enums.ReviewStatusEnum;
import com.cbk.sqlgenerate.service.TableInfoService;
import com.cbk.sqlgenerate.service.UserService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/table_info")
@Slf4j
public class TableInfoController {

    @Resource
    private TableInfoService tableInfoService;

    @Resource
    private UserService userService;

    private final static Gson GSON = new Gson();

    @PostMapping("/add")
    public BaseResponse<Long> addTableInfo(@RequestBody TableInfoAddRequest tableInfoAddRequest,
            HttpServletRequest request) {
        if (tableInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TableInfo tableInfo = new TableInfo();
        BeanUtils.copyProperties(tableInfoAddRequest, tableInfo);
        // 校验
        tableInfoService.validAndHandleTableInfo(tableInfo, true);
        User loginUser = userService.getLoginUser(request);
        tableInfo.setUserId(loginUser.getId());
        boolean result = tableInfoService.save(tableInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(tableInfo.getId());
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTableInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        TableInfo oldTableInfo = tableInfoService.getById(id);
        if (oldTableInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldTableInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = tableInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    @PostMapping("/update")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> updateTableInfo(@RequestBody TableInfoUpdateRequest tableInfoUpdateRequest) {
        if (tableInfoUpdateRequest == null || tableInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TableInfo tableInfo = new TableInfo();
        BeanUtils.copyProperties(tableInfoUpdateRequest, tableInfo);
        // 参数校验
        tableInfoService.validAndHandleTableInfo(tableInfo, false);
        long id = tableInfoUpdateRequest.getId();
        // 判断是否存在
        TableInfo oldTableInfo = tableInfoService.getById(id);
        if (oldTableInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        boolean result = tableInfoService.updateById(tableInfo);
        return ResultUtils.success(result);
    }

    @GetMapping("/get")
    public BaseResponse<TableInfo> getTableInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TableInfo tableInfo = tableInfoService.getById(id);
        return ResultUtils.success(tableInfo);
    }

    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<TableInfo>> listTableInfo(TableInfoQueryRequest tableInfoQueryRequest) {
        List<TableInfo> tableInfoList = tableInfoService.list(getQueryWrapper(tableInfoQueryRequest));
        return ResultUtils.success(tableInfoList);
    }

    @GetMapping("/list/page")
    public BaseResponse<Page<TableInfo>> listTableInfoByPage(TableInfoQueryRequest tableInfoQueryRequest,
            HttpServletRequest request) {
        long current = tableInfoQueryRequest.getCurrent();
        long size = tableInfoQueryRequest.getPageSize();
        // 限制爬虫
        if (size > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<TableInfo> tableInfoPage = tableInfoService.page(new Page<>(current, size),
                getQueryWrapper(tableInfoQueryRequest));
        return ResultUtils.success(tableInfoPage);
    }

    @GetMapping("/my/list")
    public BaseResponse<List<TableInfo>> listMyTableInfo(TableInfoQueryRequest tableInfoQueryRequest,
            HttpServletRequest request) {
        TableInfo tableInfoQuery = new TableInfo();
        if (tableInfoQueryRequest != null) {
            BeanUtils.copyProperties(tableInfoQueryRequest, tableInfoQuery);
        }
        // 先查询所有审核通过的
        tableInfoQuery.setReviewStatus(ReviewStatusEnum.PASS.getValue());
        QueryWrapper<TableInfo> queryWrapper = getQueryWrapper(tableInfoQueryRequest);
        final String[] fields = new String[]{"id", "name"};
        queryWrapper.select(fields);
        List<TableInfo> tableInfoList = tableInfoService.list(queryWrapper);
        // 再查所有本人的
        try {
            User loginUser = userService.getLoginUser(request);
            tableInfoQuery.setReviewStatus(null);
            tableInfoQuery.setUserId(loginUser.getId());
            queryWrapper = new QueryWrapper<>(tableInfoQuery);
            queryWrapper.select(fields);
            tableInfoList.addAll(tableInfoService.list(queryWrapper));
        } catch (Exception e) {
            // 未登录
        }
        // 根据 id 去重
        List<TableInfo> resultList = tableInfoList.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(TableInfo::getId))), ArrayList::new));
        return ResultUtils.success(resultList);
    }

    @GetMapping("/my/list/page")
    public BaseResponse<Page<TableInfo>> listMyTableInfoByPage(TableInfoQueryRequest tableInfoQueryRequest,
            HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        long current = tableInfoQueryRequest.getCurrent();
        long size = tableInfoQueryRequest.getPageSize();
        // 限制爬虫
        if (size > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<TableInfo> queryWrapper = getQueryWrapper(tableInfoQueryRequest);
        queryWrapper.eq("userId", loginUser.getId())
                .or()
                .eq("reviewStatus", ReviewStatusEnum.PASS.getValue());
        Page<TableInfo> tableInfoPage = tableInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(tableInfoPage);
    }

    @GetMapping("/my/add/list/page")
    public BaseResponse<Page<TableInfo>> listMyAddTableInfoByPage(TableInfoQueryRequest tableInfoQueryRequest,
            HttpServletRequest request) {
        if (tableInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        tableInfoQueryRequest.setUserId(loginUser.getId());
        long current = tableInfoQueryRequest.getCurrent();
        long size = tableInfoQueryRequest.getPageSize();
        // 限制爬虫
        if (size > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<TableInfo> tableInfoPage = tableInfoService.page(new Page<>(current, size),
                getQueryWrapper(tableInfoQueryRequest));
        return ResultUtils.success(tableInfoPage);
    }

    @PostMapping("/generate/sql")
    public BaseResponse<String> generateCreateSql(@RequestBody long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TableInfo tableInfo = tableInfoService.getById(id);
        if (tableInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        TableSchema tableSchema = GSON.fromJson(tableInfo.getContent(), TableSchema.class);
        SqlBuilder sqlBuilder = new SqlBuilder();
        return ResultUtils.success(sqlBuilder.buildCreateTableSql(tableSchema));
    }

    private QueryWrapper<TableInfo> getQueryWrapper(TableInfoQueryRequest tableInfoQueryRequest) {
        if (tableInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        TableInfo tableInfoQuery = new TableInfo();
        BeanUtils.copyProperties(tableInfoQueryRequest, tableInfoQuery);
        String sortField = tableInfoQueryRequest.getSortField();
        String sortOrder = tableInfoQueryRequest.getSortOrder();
        String name = tableInfoQuery.getName();
        String content = tableInfoQuery.getContent();
        // name、content 需支持模糊搜索
        tableInfoQuery.setName(null);
        tableInfoQuery.setContent(null);
        QueryWrapper<TableInfo> queryWrapper = new QueryWrapper<>(tableInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

}
