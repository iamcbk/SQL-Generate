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
import com.cbk.sqlgenerate.core.schema.TableSchema.Field;
import com.cbk.sqlgenerate.exception.BusinessException;
import com.cbk.sqlgenerate.model.dto.FieldInfoAddRequest;
import com.cbk.sqlgenerate.model.dto.FieldInfoQueryRequest;
import com.cbk.sqlgenerate.model.dto.FieldInfoUpdateRequest;
import com.cbk.sqlgenerate.model.entity.FieldInfo;
import com.cbk.sqlgenerate.model.entity.User;
import com.cbk.sqlgenerate.model.enums.ReviewStatusEnum;
import com.cbk.sqlgenerate.service.FieldInfoService;
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
@RequestMapping("/field_info")
@Slf4j
public class FieldInfoController {

    @Resource
    private FieldInfoService fieldInfoService;

    @Resource
    private UserService userService;

    private final static Gson GSON = new Gson();

    @PostMapping("/add")
    public BaseResponse<Long> addFieldInfo(@RequestBody FieldInfoAddRequest fieldInfoAddRequest,
                                           HttpServletRequest request) {
        if (fieldInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        FieldInfo fieldInfo = new FieldInfo();
        BeanUtils.copyProperties(fieldInfoAddRequest, fieldInfo);
        // 校验
        fieldInfoService.validAndHandleFieldInfo(fieldInfo, true);
        User loginUser = userService.getLoginUser(request);
        fieldInfo.setUserId(loginUser.getId());
        boolean result = fieldInfoService.save(fieldInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(fieldInfo.getId());
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteFieldInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        FieldInfo oldFieldInfo = fieldInfoService.getById(id);
        if (oldFieldInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldFieldInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = fieldInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    @PostMapping("/update")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> updateFieldInfo(@RequestBody FieldInfoUpdateRequest fieldInfoUpdateRequest) {
        if (fieldInfoUpdateRequest == null || fieldInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        FieldInfo fieldInfo = new FieldInfo();
        BeanUtils.copyProperties(fieldInfoUpdateRequest, fieldInfo);
        // 参数校验
        fieldInfoService.validAndHandleFieldInfo(fieldInfo, false);
        long id = fieldInfoUpdateRequest.getId();
        // 判断是否存在
        FieldInfo oldFieldInfo = fieldInfoService.getById(id);
        if (oldFieldInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        boolean result = fieldInfoService.updateById(fieldInfo);
        return ResultUtils.success(result);
    }

    @GetMapping("/get")
    public BaseResponse<FieldInfo> getFieldInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        FieldInfo fieldInfo = fieldInfoService.getById(id);
        return ResultUtils.success(fieldInfo);
    }

    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<FieldInfo>> listFieldInfo(FieldInfoQueryRequest fieldInfoQueryRequest) {
        List<FieldInfo> fieldInfoList = fieldInfoService.list(getQueryWrapper(fieldInfoQueryRequest));
        return ResultUtils.success(fieldInfoList);
    }

    @GetMapping("/list/page")
    public BaseResponse<Page<FieldInfo>> listFieldInfoByPage(FieldInfoQueryRequest fieldInfoQueryRequest,
                                                             HttpServletRequest request) {
        long current = fieldInfoQueryRequest.getCurrent();
        long size = fieldInfoQueryRequest.getPageSize();
        // 限制爬虫
        if (size > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<FieldInfo> fieldInfoPage = fieldInfoService.page(new Page<>(current, size),
                getQueryWrapper(fieldInfoQueryRequest));
        return ResultUtils.success(fieldInfoPage);
    }

    @GetMapping("/my/list")
    public BaseResponse<List<FieldInfo>> listMyFieldInfo(FieldInfoQueryRequest fieldInfoQueryRequest,
                                                         HttpServletRequest request) {
        FieldInfo fieldInfoQuery = new FieldInfo();
        if (fieldInfoQueryRequest != null) {
            BeanUtils.copyProperties(fieldInfoQueryRequest, fieldInfoQuery);
        }
        // 先查询所有审核通过的
        fieldInfoQuery.setReviewStatus(ReviewStatusEnum.PASS.getValue());
        QueryWrapper<FieldInfo> queryWrapper = getQueryWrapper(fieldInfoQueryRequest);
        final String[] fields = new String[]{"id", "name"};
        queryWrapper.select(fields);
        List<FieldInfo> fieldInfoList = fieldInfoService.list(queryWrapper);
        // 再查所有本人的
        try {
            User loginUser = userService.getLoginUser(request);
            fieldInfoQuery.setReviewStatus(null);
            fieldInfoQuery.setUserId(loginUser.getId());
            queryWrapper = new QueryWrapper<>(fieldInfoQuery);
            queryWrapper.select(fields);
            fieldInfoList.addAll(fieldInfoService.list(queryWrapper));
        } catch (Exception e) {
            // 未登录
        }
        // 根据 id 去重
        List<FieldInfo> resultList = fieldInfoList.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(FieldInfo::getId))), ArrayList::new));
        return ResultUtils.success(resultList);
    }

    @GetMapping("/my/list/page")
    public BaseResponse<Page<FieldInfo>> listMyFieldInfoByPage(FieldInfoQueryRequest fieldInfoQueryRequest,
                                                               HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        long current = fieldInfoQueryRequest.getCurrent();
        long size = fieldInfoQueryRequest.getPageSize();
        // 限制爬虫
        if (size > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<FieldInfo> queryWrapper = getQueryWrapper(fieldInfoQueryRequest);
        queryWrapper.eq("userId", loginUser.getId())
                .or()
                .eq("reviewStatus", ReviewStatusEnum.PASS.getValue());
        Page<FieldInfo> fieldInfoPage = fieldInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(fieldInfoPage);
    }

    @GetMapping("/my/add/list/page")
    public BaseResponse<Page<FieldInfo>> listMyAddFieldInfoByPage(FieldInfoQueryRequest fieldInfoQueryRequest,
                                                                  HttpServletRequest request) {
        if (fieldInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        fieldInfoQueryRequest.setUserId(loginUser.getId());
        long current = fieldInfoQueryRequest.getCurrent();
        long size = fieldInfoQueryRequest.getPageSize();
        // 限制爬虫
        if (size > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<FieldInfo> fieldInfoPage = fieldInfoService.page(new Page<>(current, size),
                getQueryWrapper(fieldInfoQueryRequest));
        return ResultUtils.success(fieldInfoPage);
    }

    @PostMapping("/generate/sql")
    public BaseResponse<String> generateCreateSql(@RequestBody long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        FieldInfo fieldInfo = fieldInfoService.getById(id);
        if (fieldInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        Field field = GSON.fromJson(fieldInfo.getContent(), Field.class);
        SqlBuilder sqlBuilder = new SqlBuilder();
        return ResultUtils.success(sqlBuilder.buildCreateFieldSql(field));
    }

    private QueryWrapper<FieldInfo> getQueryWrapper(FieldInfoQueryRequest fieldInfoQueryRequest) {
        if (fieldInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        FieldInfo fieldInfoQuery = new FieldInfo();
        BeanUtils.copyProperties(fieldInfoQueryRequest, fieldInfoQuery);
        String searchName = fieldInfoQueryRequest.getSearchName();
        String sortField = fieldInfoQueryRequest.getSortField();
        String sortOrder = fieldInfoQueryRequest.getSortOrder();
        String name = fieldInfoQuery.getName();
        String content = fieldInfoQuery.getContent();
        String fieldName = fieldInfoQuery.getFieldName();
        // name、fieldName、content 需支持模糊搜索
        fieldInfoQuery.setName(null);
        fieldInfoQuery.setFieldName(null);
        fieldInfoQuery.setContent(null);
        QueryWrapper<FieldInfo> queryWrapper = new QueryWrapper<>(fieldInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(fieldName), "fieldName", fieldName);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        // 同时按 name、fieldName 搜索
        if (StringUtils.isNotBlank(searchName)) {
            queryWrapper.like("name", searchName).or().like("fieldName", searchName);
        }
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

}
