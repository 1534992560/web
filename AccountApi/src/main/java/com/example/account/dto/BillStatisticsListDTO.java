package com.example.account.dto;

import lombok.Data;

import java.util.List;

@Data
public class BillStatisticsListDTO {
    //时间list
    private List<String> dateList;
    //收入list
    private List<Float> incomeList;
    //支出
    private List<Float> expendList;
}
