package com.example.account.service;

import com.example.account.param.BillManageParam;
import com.example.account.response.Result;


public interface BillManageService {

    /**
     * 新增账单
     */
    Result addBill(BillManageParam param);

    /**
     * 查询账单
     */
    Result queryBills(BillManageParam param);

    /**
     * 修改账单
     */
    Result updateBill(BillManageParam param);

    /**
     * 删除账单
     */
    Result removeBill(BillManageParam param);

}
