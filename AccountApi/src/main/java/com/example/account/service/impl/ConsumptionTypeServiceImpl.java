package com.example.account.service.impl;

import com.example.account.entity.ConsumptionType;
import com.example.account.enums.ReturnCode;
import com.example.account.mapper.ConsumptionTypeMapper;
import com.example.account.param.ConsumptionTypeParam;
import com.example.account.param.UpdateConsumptionTypeParam;
import com.example.account.response.Result;
import com.example.account.service.ConsumptionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ConsumptionTypeServiceImpl implements ConsumptionTypeService {

    @Autowired
    private ConsumptionTypeMapper consumptionTypeMapper;

    @Override
    public Result queryConsumptionType(ConsumptionTypeParam param) {

        List<ConsumptionType> list = consumptionTypeMapper.selectByHouseId(param.getHouseId());
        return Result.success(list);
    }

    @Override
    public Result addConsumptionType(ConsumptionTypeParam param) {

        ConsumptionType type = consumptionTypeMapper.selectByName(param.getName());
        ConsumptionType record = new ConsumptionType();

        if (null != type){
            return Result.failure(ReturnCode.ADD_CONSUMTYPE_FAIL);
        }

        Date date = new Date();
        record.setName(param.getName());
        record.setHouseId(param.getHouseId());
        record.setIsCustom(1);
        record.setCreateTime(date);
        record.setUpdateTime(date);
        try {
            consumptionTypeMapper.insert(record);
            return Result.success(record);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ReturnCode.ADD_CONSUMTYPE_FAIL);
        }
    }

    @Override
    public Result updateConsumptionType(UpdateConsumptionTypeParam param) {
        ConsumptionType record = consumptionTypeMapper.selectByPrimaryKey(param.getId());
        if (record == null || record.getIsCustom() == 0) {
            return Result.failure(ReturnCode.UPDATE_CONSUMTYPE_FAIL);
        }

        ConsumptionType consumptionType = consumptionTypeMapper.selectByName(param.getNewName());
        if(null != consumptionType){
            //避免修改的类型与其他类型重名
            Result.failure(ReturnCode.ADD_CONSUMTYPE_FAIL);
        }

        record.setName(param.getNewName());
        record.setUpdateTime(new Date());
        consumptionTypeMapper.updateByPrimaryKeySelective(record);

        return Result.success(record);
    }

    @Override
    public Result deleteConsumptionType(ConsumptionTypeParam param) {
        ConsumptionType record = consumptionTypeMapper.selectByPrimaryKey(param.getId());
        if (record == null || record.getIsCustom() == 0) {
            return Result.failure(ReturnCode.DELETE_CONSUMTYPE_FAIL);
        }

        record.setIsDelete(1);
        try {
            consumptionTypeMapper.updateByPrimaryKeySelective(record);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(ReturnCode.DELETE_CONSUMTYPE_FAIL);
        }
    }
}
