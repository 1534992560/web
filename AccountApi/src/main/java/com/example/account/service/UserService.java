package com.example.account.service;


import com.example.account.entity.User;
import com.example.account.param.BaseUserParam;
import com.example.account.param.RemoveUserParam;
import com.example.account.param.TransferUserParam;
import com.example.account.param.UpdateUserParam;
import com.example.account.response.Result;

import java.util.List;

public interface UserService {
    /**
     * 注册管理员
     */
    Result register(BaseUserParam param);

    /**
     * 添加普通用户
     */
    Result addUser(BaseUserParam param);

    /**
     * 查询用户
     * @param param
     * @return
     */
    Result queryUser(BaseUserParam param);

    //登录
    Result loginUser(BaseUserParam param);

    //普通用户修改自己密码
    Result updateMyPsw(UpdateUserParam param);

    //移除家庭成员
    Result removeUser(RemoveUserParam param);

    //转让家庭账号
    Result transferAdmin(TransferUserParam param);


    /**
     * 重置密码
     * @param param
     * @return
     */
    Result resetPassword(BaseUserParam param);

    /**
     * 通过邮箱重置密码
     * @param param 包含邮箱、验证码和新密码
     * @return Result
     */
    Result resetPasswordByEmail(BaseUserParam param);

    /**
     * 是否管理员
     * @param id
     * @return
     */
    boolean isAdmin(Integer id);

    /**
     * 检查邮箱是否已被注册
     * @param email 邮箱
     * @return Result
     */
    Result checkEmail(String email);
}
