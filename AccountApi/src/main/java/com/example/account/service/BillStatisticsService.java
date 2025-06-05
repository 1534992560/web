package com.example.account.service;

import com.example.account.param.BillManageParam;
import com.example.account.response.Result;

public interface BillStatisticsService {

    /**
     * 根据时间统计
     */
    Result statisticsByTime(BillManageParam param);

    /**
     * 根据时间统计各类型的花费
     * @param param
     * @return
     */
    Result statisticsTypeByTime(BillManageParam param);

    /**
     * 统计折线图(根据时间统计)
     * @param param
     * @return
     */
    Result statisticsLineChartByTime(BillManageParam param);

}
