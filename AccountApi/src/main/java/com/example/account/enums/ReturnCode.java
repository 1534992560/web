package com.example.account.enums;

public enum ReturnCode {
    SUCCESS("200", "success"),
    PARAM_VALID_ERROR("400", "参数验证错误"),
    SYSTEM_ERROR("500", "系统错误"),
    //用户模块
    REGISTER_FAIL("201", "注册失败，用户名已存在"),
    RESET_PASSWORD_FAIL("205", "重置密码失败"),
    DELETE_USER_FAIL("204", "删除用户失败"),
    PERMISSION_DENIED("203", "权限不足"),
    LOGIN_FAIL("202", "登录失败，用户名或密码错误"),
    TRANSFER_ADMIN_FAIL("207", "转让管理员失败"),
    UPDATE_PASSWORD_FAIL("206", "修改密码失败"),
    ADD_CONSUMTYPE_FAIL("E10008", "该类型已经存在！"),
    QUERY_CONSUMTYPE_FAIL("E10009", "搜索有误！"),
    UPDATE_CONSUMTYPE_FAIL("E10010", "您无权修改该类型！"),
    DELETE_CONSUMTYPE_FAIL("E10011", "删除该类型失败！"),
    VERIFICATION_CODE_ERROR("208", "验证码错误"),
    VERIFICATION_CODE_EXPIRED("E10013", "验证码已过期"),
    EMAIL_EXISTS("209", "邮箱已被注册"),
    EMAIL_NOT_EXISTS("210", "邮箱不存在"),
    PARAM_ERROR("211", "参数错误"),

    //账单模块
    ADD_BILL_FAIL("E20001", "新增账单失败"),
    UPDATE_BILL_FAIL("E20002", "修改账单失败"),
    DELETE_BILL_FAIL("E20003", "删除账单失败"),
    UPDATE_BUDGET_FAIL("E20004", "设置预算失败");

    /**
     * 状态码
     */
    String code;

    /**
     * 状态信息
     */
    String msg;

    ReturnCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
