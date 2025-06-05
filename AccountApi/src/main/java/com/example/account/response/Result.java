package com.example.account.response;

import com.example.account.enums.ReturnCode;
import lombok.Data;

@Data
public class Result<T> {

    /**
     * 响应状态
     */
    private String status;

    /**
     * 描述
     */
    private String description;

    /**
     * 响应结果
     */
    private Object data;

    public Result(String status, String description, Object data) {
        this.status = status;
        this.description = description;
        this.data = data;
    }

    /**
     * 返回成功信息
     */
    public static Result success(){
        return new Result(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMsg(), null);
    }

    /**
     * 返回成功的对象
     */
    public static Result success(Object data){
        return new Result(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMsg(), data);
    }

    /**
     * 返回失败信息
     */
    public static Result failure(ReturnCode returnCode){
        return new Result(returnCode.getCode(), returnCode.getMsg(), null);
    }

    /**
     * 返回失败对象
     */
    public static Result failure(ReturnCode returnCode, Object data){
        return new Result(returnCode.getCode(), returnCode.getMsg(), data);
    }

}
