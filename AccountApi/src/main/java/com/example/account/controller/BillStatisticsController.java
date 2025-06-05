package com.example.account.controller;

import com.example.account.param.BillManageParam;
import com.example.account.response.Result;
import com.example.account.service.BillStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillStatisticsController {
    @Autowired
    private BillStatisticsService billStatisticsService;

    @GetMapping("/bill/statisticsByTime")
    public Result statisticsBillsByTime(BillManageParam param){
        return billStatisticsService.statisticsByTime(param);
    }

    @GetMapping("/bill/statisticsType")
    public Result statisticsTypeByTime(BillManageParam param){
        return billStatisticsService.statisticsTypeByTime(param);
    }

    @GetMapping("/bill/statisticsLineChart")
    public Result statisticsLineChart(BillManageParam param){
        return billStatisticsService.statisticsLineChartByTime(param);
    }
}
