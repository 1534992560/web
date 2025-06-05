package com.example.account.dto;

import lombok.Data;

@Data
public class StatisticsByTypeDTO {
    /**
     * 类型名称
     */
    private String name;

    /**
     * 金额
     */
    private float money;
}
