package com.example.account.service;

import com.example.account.param.ConsumptionTypeParam;
import com.example.account.param.UpdateConsumptionTypeParam;
import com.example.account.response.Result;



public interface ConsumptionTypeService {

    //查询所有消费类型
    Result queryConsumptionType(ConsumptionTypeParam param);

    //增加消费类型
    Result addConsumptionType(ConsumptionTypeParam param);

    //修改消费类型
    Result updateConsumptionType(UpdateConsumptionTypeParam param);

    //删除消费类型
    Result deleteConsumptionType(ConsumptionTypeParam param);

}
