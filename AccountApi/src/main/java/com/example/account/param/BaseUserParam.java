package com.example.account.param;

import lombok.Data;

@Data
public class BaseUserParam extends BasePageParam{
    private Integer id;

    private String name;

    private String password;

    private String newPassword;

    private Integer isAdmin;

    private Integer houseId;

    private float budget;

    private int pageNum;

    private int pageSize;

    private String email;

    private String verificationCode;

    private Integer targetUserId;  // 目标用户ID

    public Integer getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(Integer targetUserId) {
        this.targetUserId = targetUserId;
    }
}
