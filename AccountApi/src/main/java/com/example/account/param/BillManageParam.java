package com.example.account.param;

import lombok.Data;

import java.util.Date;

@Data
public class BillManageParam extends BasePageParam{
    /**
     * 账单Id
     */
    private Integer id;

    /**
     * 登录人Id
     */
    private Integer userId;

    /**
     * 管理员是否只看自己的账单，1是，0否
     */
    private boolean onlySelf;

    /**
     * 记账人Id（条件筛选使用）
     */
    private Integer recordId;

    /**
     * 家庭Id
     */
    private Integer houseId;

    /**
     * 消费类型Id
     */
    private Integer consumptionId;

    /**
     * 收入类型Id
     */
    private Integer incomeId;

    /**
     * 账单类型 1.消费 2.收入
     */
    private Integer billType;

    /**
     * 金额
     */
    private float money;

    /**
     * 备注
     */
    private String remark;

    /**
     * 记账日期
     */
    private String recordTime;

    /**
     * 记账日期
     */
    private Date recordDate;


    /**
     * 开始日期(自定义)
     */
    private String beginDate;

    /**
     * 结束日期(自定义)
     */
    private String endDate;

    /**
     * 开始日期
     */
    private Date beginTime;

    /**
     * 结束日期
     */
    private Date endTime;

    /**
     * 时间类型：1.本月,2.近三月,3.近半年,4.近一年
     */
    private Integer timeType;

    /**
     * 预算
     */
    private float budget;
}
