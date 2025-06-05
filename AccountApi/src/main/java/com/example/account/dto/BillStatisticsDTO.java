package com.example.account.dto;

import lombok.Data;

@Data
public class BillStatisticsDTO {
    /**
     * 支出总额
     */
    private float totalExpend;

    /**
     * 收入总额
     */
    private float totalIncome;

    /**
     * 预算总额
     */
    private float totalBudget;
}
