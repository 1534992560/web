package com.example.account.service.impl;

import com.example.account.dto.QueryBillsDTO;
import com.example.account.entity.Bill;
import com.example.account.entity.House;
import com.example.account.enums.ReturnCode;
import com.example.account.mapper.BillMapper;
import com.example.account.mapper.HouseMapper;
import com.example.account.param.BillManageParam;
import com.example.account.response.PageResult;
import com.example.account.response.Result;
import com.example.account.service.BillManageService;
import com.example.account.service.UserService;
import com.example.account.utils.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public class BillManageServiceImpl implements BillManageService {
    @Autowired
    private BillMapper billMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private HouseMapper houseMapper;

    @Override
    public Result addBill(BillManageParam param) {
        Date date = new Date();
        //新增消费账单
        Bill record = new Bill();
        record.setConsumptionId(param.getConsumptionId());
        record.setUserId(param.getUserId());
        record.setHouseId(param.getHouseId());
        record.setMoney(param.getMoney());
        record.setRemark(param.getRemark());
        record.setRecordTime(param.getRecordTime() == null ? date : DateUtil.stringToDate(param.getRecordTime(), DateUtil.fullDayFormat));
        record.setBillType(param.getBillType());
        record.setCreateTime(date);
        record.setUpdateTime(date);
        record.setIsDelete(0);
        int i = billMapper.insertSelective(record);

        if(i > 0){
            return Result.success();
        }else{
            return Result.failure(ReturnCode.ADD_BILL_FAIL);
        }
    }

    @Override
    public Result queryBills(BillManageParam param) {
        String format = "yyyy-MM-dd HH:mm:ss";
//        boolean isAdmin = userService.isAdmin(param.getUserId());
        boolean isAdmin = false;
        if(param.getUserId() == -1){
            isAdmin = true;
        }
        if(isAdmin){
            param.setUserId(null);
        }
        if(!StringUtils.isBlank(param.getBeginDate()) && !StringUtils.isBlank(param.getEndDate())) {
            try {
                param.setBeginTime(DateUtils.parseDate(param.getBeginDate(), format));
                param.setEndTime(DateUtils.parseDate(param.getEndDate(), format));
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println("转换日期格式失败！");
            }
        }

        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<QueryBillsDTO> list = billMapper.selectBillDetails(param);

        PageInfo<QueryBillsDTO> pageInfo=new PageInfo<QueryBillsDTO>(list);
        PageResult result = new PageResult(pageInfo.getTotal(), pageInfo.getList());

        return Result.success(result);
    }

    @Override
    public Result updateBill(BillManageParam param) {
        Bill record = new Bill();
        record.setId(param.getId());
        record.setConsumptionId(param.getConsumptionId());
        record.setRemark(param.getRemark());
        record.setMoney(param.getMoney());
        record.setRecordTime(DateUtil.stringToDate(param.getRecordTime(), DateUtil.fullDayFormat));
        record.setUpdateTime(new Date());
        int i = billMapper.updateByPrimaryKeySelective(record);
        if(i <= 0){
            return Result.failure(ReturnCode.UPDATE_BILL_FAIL);
        }
        return Result.success();
    }

    @Override
    public Result removeBill(BillManageParam param) {
        Bill record = new Bill();
        record.setId(param.getId());
        record.setUpdateTime(new Date());
        record.setIsDelete(1);
        int i = billMapper.updateByPrimaryKeySelective(record);
        if(i <= 0){
            return Result.failure(ReturnCode.DELETE_BILL_FAIL);
        }
        return Result.success();
    }
}
