package com.example.account.service.impl;

import com.example.account.dto.BillStatisticsDTO;
import com.example.account.dto.BillStatisticsListDTO;
import com.example.account.dto.StatisticsByTypeDTO;
import com.example.account.entity.House;
import com.example.account.mapper.BillMapper;
import com.example.account.mapper.HouseMapper;
import com.example.account.param.BillManageParam;
import com.example.account.response.Result;
import com.example.account.service.BillStatisticsService;
import com.example.account.service.UserService;
import com.example.account.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BillStatisticsServiceImpl implements BillStatisticsService {
    @Autowired
    private BillMapper billMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private HouseMapper houseMapper;

    @Override
    public Result statisticsByTime(BillManageParam param) {
        boolean isAdmin = false;
        if(param.getUserId() == -1){
            isAdmin = true;
        }
        if(isAdmin){
            //管理员默认展示家庭总账单
            param.setUserId(null);
        }
        BillStatisticsDTO dto = new BillStatisticsDTO();
        if (param.getTimeType() == 1) {
            House house = houseMapper.selectByPrimaryKey(param.getHouseId());
            dto.setTotalBudget(house.getBudget());
        }

        BillManageParam manageParam = convertParam(param);

        //查询总消费
        manageParam.setBillType(1);
        manageParam.setUserId(param.getUserId());
        Float expendMoney = billMapper.selectBillTotalMoney(manageParam);
        if(null != expendMoney) {
            dto.setTotalExpend(expendMoney);
        }

        //查询总支出
        manageParam.setBillType(2);
        Float incomeMoney = billMapper.selectBillTotalMoney(manageParam);
        if(null != incomeMoney) {
            dto.setTotalIncome(incomeMoney);
        }

        return Result.success(dto);
    }


    private BillManageParam convertParam(BillManageParam param) {

        boolean isAdmin = false;
    
        if(param.getUserId() == null){
            isAdmin = true;
        }else {
            if(param.getUserId() == -1){
                isAdmin = true;
            }
        }
        if(isAdmin && !param.isOnlySelf()){
            param.setUserId(null);
        }

        Date today = DateUtil.getToday();
        Date day = null;
        if (param.getTimeType() == 1) {
            //查询当月的账单
            day = DateUtil.getFirstDayOfMonth(today);
        } else if (param.getTimeType() == 2) {
            //查询近三个月的账单
            day = DateUtils.addMonths(today, -3);
        } else if (param.getTimeType() == 3) {
            //查询近半年的账单
            day = DateUtils.addMonths(today, -6);
        } else if (param.getTimeType() == 4) {
            //查询近一年的账单
            day = DateUtils.addYears(today, -1);
        } else {
            //自定义日期使用
            if (!StringUtils.isBlank(param.getBeginDate()) && !StringUtils.isBlank(param.getEndDate())) {
                param.setBeginTime(DateUtil.stringToDate(param.getBeginDate(), DateUtil.fullDayFormat));
                param.setEndTime(DateUtil.stringToDate(param.getEndDate(), DateUtil.fullDayFormat));
            }
        }
        param.setBeginTime(day);
        return param;
    }

    @Override
    public Result statisticsTypeByTime(BillManageParam param) {
        boolean isAdmin = false;
        if(param.getUserId() == null){
            isAdmin = true;
        }else {
            if(param.getUserId() == -1){
                isAdmin = true;
            }
        }
        if(isAdmin && !param.isOnlySelf()){
            //管理员默认展示家庭总账单
            param.setUserId(null);
        }
        //转换入参
        BillManageParam manageParam = convertParam(param);
        
        // 设置结束时间为今天的23:59:59
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtil.getToday());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.SECOND, -1);
        manageParam.setEndTime(cal.getTime());
        
        //统计各个类型的花费
        List<StatisticsByTypeDTO> list = billMapper.selectBillTotalMoneyByType(manageParam);
        return Result.success(list);
    }

    @Override
    public Result statisticsLineChartByTime(BillManageParam param) {
        boolean isAdmin = userService.isAdmin(param.getUserId());
        if(isAdmin && !param.isOnlySelf()){
            //管理员默认展示家庭总账单
            param.setUserId(null);
        }
        //时间list
        List<String> dateList = new ArrayList<>();
        //收入list
        List<Float> incomeList = new ArrayList<>();
        //支出
        List<Float> expendList = new ArrayList<>();

        BillManageParam manageParam = convertParam(param);
        Date sDate =  param.getBeginTime();//开始日期
        Date eDate = DateUtils.addDays(DateUtil.getToday(), 1);

        //获取数据
        setBillMoney(manageParam, sDate, eDate, dateList, incomeList, expendList);
        //返回数据
        BillStatisticsListDTO dto = new BillStatisticsListDTO();
        dto.setDateList(dateList);
        dto.setIncomeList(incomeList);
        dto.setExpendList(expendList);
        return Result.success(dto);
    }

    private void setBillMoney(BillManageParam param, Date sDate, Date eDate, List<String> dateList, List<Float> incomeList, List<Float> expendList) {
        if(param.getTimeType() != 1){
            sDate = DateUtils.addDays(sDate, 1);
        }
        while (!DateUtils.isSameDay(sDate, eDate)) {
            param.setBeginTime(sDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(sDate);
            if(param.getTimeType() == 1) {
                dateList.add(cal.get(Calendar.DAY_OF_MONTH) + "");
                sDate = DateUtils.addDays(sDate,1);
            }else{
                int sMonth = cal.get(Calendar.MONTH) + 1;
                sDate = DateUtils.addMonths(sDate, 1);
                cal.setTime(sDate);
                int eMonth = cal.get(Calendar.MONTH) + 1;
                dateList.add(sMonth + "-" + eMonth);
            }
            param.setEndTime(sDate);
            //支出
            param.setBillType(1);
            Float expendMoney = billMapper.selectBillTotalMoney(param);
            if(null == expendMoney){
                expendMoney = 0.00f;
            }
            expendList.add(expendMoney);

            //收入
            param.setBillType(2);
            Float incomeMoney = billMapper.selectBillTotalMoney(param);
            if(null == incomeMoney){
                incomeMoney = 0.00f;
            }
            incomeList.add(incomeMoney);
        }
    }
}
