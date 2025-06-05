package com.example.account.controller;

import com.example.account.param.BaseUserParam;
import com.example.account.param.RemoveUserParam;
import com.example.account.param.TransferUserParam;
import com.example.account.param.UpdateUserParam;
import com.example.account.response.Result;
import com.example.account.service.EmailService;
import com.example.account.service.UserService;
import com.example.account.enums.ReturnCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    /**
     * 注册管理员
     */
    @PostMapping("/user/register")
    public Result register(@RequestBody BaseUserParam param){
        return userService.register(param);
    }

    /**
     * 查询用户
     */
    @GetMapping("/user/query")
    public Result queryUser(BaseUserParam param){
        return userService.queryUser(param);
    }

    /**
     * 增加普通用户
     */
    @PostMapping("/user/add")
    public Result addUser(@RequestBody BaseUserParam param, @RequestHeader("userId") Integer userId){
        param.setId(userId);  // 设置当前操作用户的ID
        return userService.addUser(param);
    }

    /**
     * 重置密码
     */
    @PutMapping("/user/resetPwd")
    public Result resetPassword(@RequestBody BaseUserParam param, @RequestHeader("userId") Integer userId){
        param.setId(userId);  // 设置当前操作用户的ID
        return userService.resetPassword(param);
    }

    //登录

    @PostMapping("/user/login")
    public Result loginUser(@RequestBody(required = true) BaseUserParam param){
        if (param == null || StringUtils.isEmpty(param.getName()) || StringUtils.isEmpty(param.getPassword())) {
            return Result.failure(ReturnCode.PARAM_ERROR);
        }
        return userService.loginUser(param);
    }

    //普通用户修改自己密码
    @PutMapping("/user/updateMyPsw")
    public Result updateMyPsw(@RequestBody UpdateUserParam param){
        return userService.updateMyPsw(param);
    }

    //移除家庭成员
    @PutMapping("/user/remove")
    public Result removeUser(@RequestBody RemoveUserParam param, @RequestHeader("userId") Integer userId){
        param.setAdminUserId(userId);  // 设置当前操作用户的ID
        return userService.removeUser(param);
    }

    //转让管理员
    @PutMapping("/user/transferAdmin")
    public Result transferAdmin(@RequestBody TransferUserParam param){
        return userService.transferAdmin(param);
    }

    //发送验证码
    @PostMapping("/user/sendVerificationCode")
    public Result sendVerificationCode(@RequestBody BaseUserParam param) {
        try {
            emailService.sendVerificationCode(param.getEmail());
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ReturnCode.VERIFICATION_CODE_ERROR);
        }
    }

    @PostMapping("/user/checkEmail")
    public Result checkEmail(@RequestBody BaseUserParam param) {
        return userService.checkEmail(param.getEmail());
    }

     //发送重置密码验证码
    @PostMapping("/user/sendResetCode")
    public Result sendResetCode(@RequestBody BaseUserParam param) {
        try {
            // 验证邮箱是否存在
            Result checkResult = userService.checkEmail(param.getEmail());
            if (!"exists".equals(checkResult.getData())) {
                return Result.failure(ReturnCode.EMAIL_NOT_EXISTS);
            }
            emailService.sendVerificationCode(param.getEmail(), true);  // 传入true表示是重置密码的验证码
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ReturnCode.VERIFICATION_CODE_ERROR);
        }
    }

    //通过邮箱重置密码
    @PostMapping("/user/resetPasswordByEmail")
    public Result resetPasswordByEmail(@RequestBody BaseUserParam param) {
        return userService.resetPasswordByEmail(param);
    }
}
