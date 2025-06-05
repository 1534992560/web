package com.example.account.param;

import lombok.Data;


@Data
public class UpdateUserParam extends BaseUserParam{

    //旧密码
    private String oldPassword;

    //新密码
    private String newPassword;
}
