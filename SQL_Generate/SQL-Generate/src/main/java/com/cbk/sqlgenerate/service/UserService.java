package com.cbk.sqlgenerate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cbk.sqlgenerate.model.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService extends IService<User> {

    long userRegister(String userName, String userAccount, String userPassword, String checkPassword, String userRole);

    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    User getLoginUser(HttpServletRequest request);

    boolean isAdmin(HttpServletRequest request);

    boolean userLogout(HttpServletRequest request);

}
