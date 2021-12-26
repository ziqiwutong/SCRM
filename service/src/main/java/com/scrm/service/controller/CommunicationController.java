package com.scrm.service.controller;

import com.scrm.service.entity.*;
import com.scrm.service.service.CommunicationLogService;
import com.scrm.service.service.CustomerService;
import com.scrm.service.util.resp.CodeEum;
import com.scrm.service.util.resp.PageResult;
import com.scrm.service.util.resp.Resp;
import com.scrm.service.util.resp.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RequestMapping(value="/communication")
@RestController
public class CommunicationController {
    @Resource
    private CommunicationLogService communicationLogService;

    @GetMapping(value="/queryCustomerRelation")
    public Result queryCustomerRelation(
            @RequestParam(value = "customerId") Long customerId,
            @RequestParam(value = "relationType", required = false) String relationType
    )
    {
        List<UserAndCommunication> userAndCommunications = communicationLogService.queryCommunicationUser(customerId);
        Communication communication = communicationLogService.queryCustomerRelationTimes(customerId);
        List final_list = new ArrayList();

        for (UserAndCommunication userAndCommunication:userAndCommunications
        ) {
            final_list.add(userAndCommunication);
        }
        final_list.add(communication);
        if(relationType == null){
            List<CustomerRelation> CustomerRelations = communicationLogService.queryCustomerRelation(customerId);
            for (CustomerRelation customerRelation:CustomerRelations
            ) {
                final_list.add(customerRelation);
            }
        }else{
            List<CustomerRelation> CustomerRelations = communicationLogService.querySingleCustomerRelation(
                    customerId, relationType);
            for (CustomerRelation customerRelation:CustomerRelations
            ) {
                final_list.add(customerRelation);
            }
        }
        return Result.success(final_list);
    }

    @GetMapping("/queryCustomerRelationDetail")
    @ResponseBody
    public Resp queryCustomerRelationDetail(
            @RequestParam(value = "id") Long id
    ) {
        CustomerRelation relation = communicationLogService.queryCustomerRelationDetail(id);
        if (relation != null) {
            return Resp.success().setData(relation);
        } else {
            return Resp.error().setMsg("获取失败");
        }
    }

    @PostMapping(value="/addCustomerRelation")
    public Result addCustomerRelation(
            @RequestBody CustomerRelation customerRelation
    )
    {
        try{
            if(customerRelation.getCommunicationTime() == null){
                Timestamp date = new Timestamp(System.currentTimeMillis());
                customerRelation.setCommunicationTime(date);
            }
            communicationLogService.addCustomerRelation(customerRelation);
            return Result.success();
        }catch(Exception e) {
            return Result.error(CodeEum.FAIL);
        }
    }

    @PostMapping(value="/editCustomerRelation")
    public Result editCustomerRelation(
            @RequestBody CustomerRelation customerRelation
    )
    {
        try{
            communicationLogService.editCustomerRelation(customerRelation);
            return Result.success();
        }catch(Exception e) {
            return Result.error(CodeEum.FAIL);
        }
    }

    @PostMapping(value="/deleteCustomerRelation")
    public Result deleteCustomerRelation(
            @RequestParam(value = "id") Long id
    )
    {
        try{
            communicationLogService.deleteCustomerRelation(id);
            return Result.success();
        }catch(Exception e) {
            return Result.error(CodeEum.FAIL);
        }
    }
}
