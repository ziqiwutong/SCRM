package com.scrm.service.controller;

import com.scrm.service.entity.*;
import com.scrm.service.service.CommunicationLogService;
import com.scrm.service.service.CommunicationService;
import com.scrm.service.util.resp.CodeEum;
import com.scrm.service.util.resp.PageResult;
import com.scrm.service.util.resp.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Timestamp;
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
            @RequestBody Communication communication
    )
    {
        if (communication == null) {
            return Result.error(CodeEum.PARAM_MISS);
        }
        try{
            communicationService.addCommunication(communication);
            return Result.success();
        }catch(Exception e) {
            return Result.error(CodeEum.FAIL);
        }
    }

    @PostMapping(value="/editCommunication")
    public Result editCommunication(
            @RequestBody Communication communication
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
            @RequestParam(value = "id") Integer id
    )
    {
        try{
            communicationService.deleteCommunication(id);
            communicationService.deleteCommunicationLog(id);
            return Result.success();
        }catch(Exception e) {
                return Result.error(CodeEum.FAIL);
        }
    }

    @GetMapping(value="/queryCommunicationLog")
    public Result queryCommunicationLog(
            @RequestParam(value = "id") Integer id
    )
    {
        UserAndCommunication userAndCommunication = communicationLogService.queryCommunicationUser(id);
        Communication communication = communicationLogService.queryCommunication(id);
        List<CommunicationLog> communicationLogs = communicationLogService.queryCommunicationLog(id);
        List final_list = new ArrayList();
        final_list.add(userAndCommunication);
        final_list.add(communication);
        for (CommunicationLog communicationLog:communicationLogs
             ) {
            final_list.add(communicationLog);
        }
        return Result.success(final_list);
    }

    @PostMapping(value="/addCommunicationLog")
    public Result addCommunicationLog(
            @RequestBody CommunicationLog communicationLog
    )
    {
        if (communicationLog == null) {
            return Result.error(CodeEum.PARAM_MISS);
        }
        try{
            communicationLogService.addCommunicationLog(communicationLog);
            communicationLogService.PlusCommunication(communicationLog.getCommunicationId(),communicationLog.getCommunicationWay());
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
            communicationLogService.editCommunicationLog(communicationLog);
            return Result.success();
        }catch(Exception e) {
            return Result.error(CodeEum.FAIL);
        }
    }

    @PostMapping(value="/deleteCommunicationLog")
    @ResponseBody
    public Result deleteCommunicationLog(
            @RequestParam(value = "id") Integer id,
            @RequestParam(value = "communicationId") Integer communicationId,
            @RequestParam(value = "communicationWay") Integer communicationWay
    )
    {
        try{
            communicationLogService.deleteCommunicationLog(id);
            communicationLogService.MinusCommunication(communicationId,communicationWay);
            return Result.success();
        }catch(Exception e) {
            return Result.error(CodeEum.FAIL);
        }
    }
}
