package com.example.account.dto;

import lombok.Data;

import java.util.Date;

@Data
public class QueryBillsDTO {
    /**
     * Id
     */
    private Integer Id;

    /**
     *  记账人Id
     */
    private Integer userId;

    /**
     * 记账人Name
     */
    private String userName;

    /**
     * 家庭Id
     */
    private Integer houseId;

    /**
     * 消费类型Id
     */
    private Integer consumptionId;

    /**
     * 消费类型Name
     */
    private String consumptionName;

    /**
     * 账单类型 1.消费 2.收入
     */
    private Integer billType;

    /**
     * 金额
     */
    private float Money;

    /**
     * 备注
     */
    private String remark;

    /**
     * 记录日期
     */
    private Date recordTime;
}
