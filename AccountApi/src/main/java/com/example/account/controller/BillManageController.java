package com.example.account.controller;

import com.example.account.param.BillManageParam;
import com.example.account.response.Result;
import com.example.account.service.BillManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BillManageController {
    @Autowired
    private BillManageService billManageService;

    @PostMapping("/bill/add")
    public Result addBill(@RequestBody BillManageParam param){
        return billManageService.addBill(param);
    }

    @GetMapping("/bill/query")
    public Result queryBills(BillManageParam param){
        return billManageService.queryBills(param);
    }

    @PutMapping("/bill/update")
    public Result updateBill(@RequestBody BillManageParam param){
        return billManageService.updateBill(param);
    }

    @PutMapping("/bill/remove")
    public Result removeBill(@RequestBody BillManageParam param){
        return billManageService.removeBill(param);
    }

}
