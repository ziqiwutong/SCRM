package com.scrm.service.controller;

import com.scrm.service.entity.*;
import com.scrm.service.service.CommunicationLogService;
import com.scrm.service.service.CommunicationService;
import com.scrm.service.util.resp.CodeEum;
import com.scrm.service.util.resp.PageResult;
import com.scrm.service.util.resp.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RequestMapping(value="/communication")
@RestController
public class CommunicationController {
    @Resource
    private CommunicationService communicationService;

    @Resource
    private CommunicationLogService communicationLogService;

    @GetMapping(value="/queryCommunication")
    public PageResult queryCommunication(
            @RequestParam(value = "pageCount", required = false) Integer pageCount,
            @RequestParam(value = "currentPage", required = false) Integer currentPage
    )
    {
        List<UserAndCommunication> userAndCommunications =
                communicationService.queryCommunication(pageCount, currentPage);
        Integer count = communicationService.queryCount();
        return PageResult.success(userAndCommunications, count, currentPage);
    }

    @GetMapping(value="/queryCommunicationByKey")
    public Result queryCommunicationByKey(
            @RequestParam(value = "keySearch") String keySearch
    )
    {
        List<UserAndCommunication> userAndCommunications = communicationService.queryCommunicationByKey(keySearch);
        if (userAndCommunications.size() == 0) {
            return Result.error(CodeEum.NOT_EXIST);
        } else {
            return Result.success(userAndCommunications);
        }
    }

    @PostMapping(value="/addCommunication")
    public Result addCommunication(
             Communication communication
    )
    {
        try{
            communicationService.addCommunication(communication);
            return Result.success();
        }catch(Exception e) {
            return Result.error(CodeEum.FAIL);
        }
    }

    @PostMapping(value="/editCommunication")
    public Result editCommunication(
             Communication communication
    )
    {
        if (communication == null) {
            return Result.error(CodeEum.PARAM_MISS);
        }
        try{
            communicationService.editCommunication(communication);
            return Result.success();
        }catch(Exception e) {
            return Result.error(CodeEum.FAIL);
        }
    }

    @PostMapping(value="/deleteCommunication")
    public Result deleteCommunication(
            @RequestParam(value = "customerId") Long customerId
    )
    {
        try{
            communicationService.deleteCommunication(customerId);
            communicationService.deleteCommunicationLog(customerId);
            return Result.success();
        }catch(Exception e) {
                return Result.error(CodeEum.FAIL);
        }
    }

    @GetMapping(value="/queryCommunicationLog")
    public Result queryCommunicationLog(
            @RequestParam(value = "customerId") Long customerId,
            @RequestParam(value = "communicationWay") Integer communicationWay
    )
    {
        List<UserAndCommunication> userAndCommunications = communicationLogService.queryCommunicationUser(customerId);
        Communication communication = communicationLogService.queryCommunication(customerId);
        List<CommunicationLog> communicationLogs = communicationLogService.queryCommunicationLog(customerId, communicationWay);
        List final_list = new ArrayList();

        for (UserAndCommunication userAndCommunication:userAndCommunications
        ) {
            final_list.add(userAndCommunication);
        }
        final_list.add(communication);
        for (CommunicationLog communicationLog:communicationLogs
             ) {
            final_list.add(communicationLog);
        }
        return Result.success(final_list);
    }

    @GetMapping(value="/queryCommunicationLogDetail")
    public Result queryCommunicationLogDetail(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "customerId") Long customerId
    )
    {
        List<UserAndCommunication> userAndCommunications = communicationLogService.queryCommunicationUser(customerId);
        CommunicationLog communicationLog = communicationLogService.queryCommunicationLogDetail(id);
        List final_list = new ArrayList();
        for (UserAndCommunication userAndCommunication:userAndCommunications
        ) {
            final_list.add(userAndCommunication);
        }
        final_list.add(communicationLog);
        return Result.success(final_list);
    }

    @PostMapping(value="/addCommunicationLog")
    public Result addCommunicationLog(
            @RequestBody CommunicationLog communicationLog
    )
    {
        List<CommunicationLog> communicationLogs = communicationLogService.queryCommunicationLog(
                communicationLog.getCustomerId(),4);
        Timestamp date = new Timestamp(System.currentTimeMillis());
        communicationLog.setCommunicationTime(date);
        if(communicationLogs.size() == 0){
            Communication communication = new Communication();
            communication.setCustomerId(communicationLog.getCustomerId());
            try{
                communicationService.addCommunication(communication);
            }catch(Exception e) {
                return Result.error(CodeEum.FAIL);
            }
        }
        if (communicationLog == null || communicationLog.getCustomerId() == null) {
            return Result.error(CodeEum.PARAM_MISS);
        }
        try{
            communicationLogService.addCommunicationLog(communicationLog);
            communicationLogService.PlusCommunication(
                    communicationLog.getCustomerId(),communicationLog.getCommunicationWay());
            return Result.success();
        }catch(Exception e) {
            return Result.error(CodeEum.FAIL);
        }
    }

    @PostMapping(value="/editCommunicationLog")
    public Result editCommunicationLog(
            @RequestBody CommunicationLog communicationLog
    )
    {
        if (communicationLog == null) {
            return Result.error(CodeEum.PARAM_MISS);
        }
        try{
            CommunicationLog communicationLogEdit = communicationLogService.queryCommunicationLogDetail(
                    communicationLog.getId());
            communicationLogService.MinusCommunication(
                    communicationLogEdit.getCustomerId(),communicationLogEdit.getCommunicationWay());

            communicationLogService.editCommunicationLog(communicationLog);
            communicationLogService.PlusCommunication(
                    communicationLog.getCustomerId(),communicationLog.getCommunicationWay());
            return Result.success();
        }catch(Exception e) {
            return Result.error(CodeEum.FAIL);
        }
    }

    @PostMapping(value="/deleteCommunicationLog")
    @ResponseBody
    public Result deleteCommunicationLog(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "customerId") Long customerId,
            @RequestParam(value = "communicationWay") Integer communicationWay
    )
    {
        List<CommunicationLog> communicationLogs = communicationLogService.queryCommunicationLog(
                customerId, 4);
        if(communicationLogs.size() == 1){
            try{
                communicationLogService.deleteCommunicationLog(id);
                communicationService.deleteCommunication(customerId);
                return Result.success();
            }catch(Exception e) {
                return Result.error(CodeEum.FAIL);
            }
        }

        try{
            communicationLogService.deleteCommunicationLog(id);
            communicationLogService.MinusCommunication(customerId,communicationWay);
            return Result.success();
        }catch(Exception e) {
            return Result.error(CodeEum.FAIL);
        }
    }
}
