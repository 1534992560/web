package com.example.account.param;

import lombok.Data;


@Data
public class RemoveUserParam {
    //删除人id
    private Integer adminUserId;

    //被删除人的id
    private Integer removeUserId;
}
