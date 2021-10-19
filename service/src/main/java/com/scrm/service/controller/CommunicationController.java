package com.scrm.service.controller;

import com.scrm.service.entity.*;
import com.scrm.service.service.CommunicationLogService;
import com.scrm.service.service.CommunicationService;
import com.scrm.service.util.resp.PageResp;
import com.scrm.service.util.resp.Resp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Date;
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
    @ResponseBody
    public PageResp queryCommunication(

    )
    {
        return PageResp.success().setData(
                communicationService.queryCommunication(1, 1)
        ).setPage(1, 1, communicationService.queryCount()).setMsg("成功");
    }

    @GetMapping(value="/queryCommunicationByKey")
    @ResponseBody
    public Resp queryCommunicationByKey(

    )
    {
        List<UserAndCommunication> userAndCommunications = communicationService.queryCommunicationByKey("用");
        if (userAndCommunications.size() == 0) {
            return Resp.error().setMsg("无数据");
        } else {
            return Resp.success().setData(userAndCommunications).setMsg("成功");
        }
    }

    @PostMapping(value="/addCommunication")
    @ResponseBody
    public Resp addCommunication(

    )
    {
        Communication communication = new Communication();
        communication.setCustomerId((long) 1);
        communication.setVisitTimes(3);
        communication.setCallTimes(4);
        communication.setMsgTimes(5);
        communication.setWxTimes(6);

        if (communication == null) {
            return Resp.error().setMsg("不能为空");
        }
        String result = communicationService.addCommunication(communication);
        if (result == null) {
            return Resp.success().setMsg("插入成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @PostMapping(value="/editCommunication")
    @ResponseBody
    public Resp editCommunication(

    )
    {
        Communication communication = new Communication();
        communication.setId((long) 1);
        communication.setCustomerId((long) 1);
        communication.setVisitTimes(9);
        communication.setCallTimes(8);
        communication.setMsgTimes(7);
        communication.setWxTimes(6);

        if (communication == null) {
            return Resp.error().setMsg("不能为空");
        }
        String result = communicationService.editCommunication(communication);
        if (result == null) {
            return Resp.success().setMsg("更新成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @PostMapping(value="/deleteCommunication")
    @ResponseBody
    public Resp deleteCommunication(

    )
    {
        String result = communicationService.deleteCommunication(2);
        if (result == null) {
            return Resp.success().setMsg("删除成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @GetMapping(value="/queryCommunicationLog")
    @ResponseBody
    public Resp queryCommunicationLog(

    )
    {
        Communication communication = communicationLogService.queryCommunication(1);
        List<CommunicationLog> communicationLogs = communicationLogService.queryCommunicationLog(1);
        List final_list = new ArrayList();
        final_list.add(communication);
        for (CommunicationLog communicationLog:communicationLogs
             ) {
            final_list.add(communicationLog);
        }
        return Resp.success().setData(
                final_list
        ).setMsg("成功");
    }

    @PostMapping(value="/addCommunicationLog")
    @ResponseBody
    public Resp addCommunicationLog(

    )
    {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        CommunicationLog communicationLog = new CommunicationLog();
        communicationLog.setCommunicationId((long) 1);
        communicationLog.setCommunicationWay(3);
        communicationLog.setCommunicationContent("new");
        communicationLog.setCommunicationTime(timestamp);

        if (communicationLog == null) {
            return Resp.error().setMsg("不能为空");
        }
        String result = communicationLogService.addCommunicationLog(communicationLog);
        if (result == null) {
            return Resp.success().setMsg("插入成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @PostMapping(value="/editCommunicationLog")
    @ResponseBody
    public Resp editCommunicationLog(

    )
    {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        CommunicationLog communicationLog = new CommunicationLog();
        communicationLog.setId((long) 1);
        communicationLog.setCommunicationId((long) 1);
        communicationLog.setCommunicationWay(3);
        communicationLog.setCommunicationContent("xiugai");
        communicationLog.setCommunicationTime(timestamp);

        if (communicationLog == null) {
            return Resp.error().setMsg("不能为空");
        }
        String result = communicationLogService.editCommunicationLog(communicationLog);
        if (result == null) {
            return Resp.success().setMsg("更新成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @PostMapping(value="/deleteCommunicationLog")
    @ResponseBody
    public Resp deleteCommunicationLog(

    )
    {
        String result = communicationLogService.deleteCommunicationLog(2);
        if (result == null) {
            return Resp.success().setMsg("删除成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }
}
