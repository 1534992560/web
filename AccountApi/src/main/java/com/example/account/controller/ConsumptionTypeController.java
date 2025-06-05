package com.example.account.controller;

import com.example.account.param.ConsumptionTypeParam;
import com.example.account.param.UpdateConsumptionTypeParam;
import com.example.account.response.Result;
import com.example.account.service.ConsumptionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
public class ConsumptionTypeController {

    @Autowired
    private ConsumptionTypeService consumptionTypeService;

    //增加消费类型
    @PostMapping("/consumptionType/add")
    public Result addConsumptionType(@RequestBody ConsumptionTypeParam param){

        return consumptionTypeService.addConsumptionType(param);
    }

    //查询所有消费类型
    @GetMapping("/consumptionType/query")
    public Result queryConsumptionType(ConsumptionTypeParam param){

        return consumptionTypeService.queryConsumptionType(param);

    }

    //修改类型名称
    @PutMapping("/consumptionType/updateName")
    public Result updateName(@RequestBody UpdateConsumptionTypeParam param){

        return consumptionTypeService.updateConsumptionType(param);
    }

    //删除类型
    @PutMapping("/consumptionType/deleteType")
    public Result deleteType(@RequestBody ConsumptionTypeParam param){

        return consumptionTypeService.deleteConsumptionType(param);
    }

}
