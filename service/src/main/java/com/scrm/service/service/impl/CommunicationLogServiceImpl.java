package com.scrm.service.service.impl;

import com.scrm.service.dao.CommunicationDao;
import com.scrm.service.dao.CommunicationLogDao;
import com.scrm.service.entity.Communication;
import com.scrm.service.entity.CommunicationLog;
import com.scrm.service.entity.UserAndCommunication;
import com.scrm.service.service.CommunicationLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommunicationLogServiceImpl implements CommunicationLogService {
    @Resource
    private CommunicationLogDao communicationLogDao;

    @Override
    public List<UserAndCommunication> queryCommunicationUser(Integer customerId) {
        return communicationLogDao.queryCommunicationUser(customerId);
    }

    @Override
    public Communication queryCommunication(Integer customerId) {
        return communicationLogDao.queryCommunication(customerId);
    }

    @Override
    public List<CommunicationLog> queryCommunicationLog(Integer id, Integer customerId, Integer communicationWay) {
        return communicationLogDao.queryCommunicationLog(id, customerId, communicationWay);
    }

    @Override
    public String addCommunicationLog(CommunicationLog communicationLog) throws Exception{
        int result = communicationLogDao.addCommunicationLog(communicationLog);
        if (result < 1) {
            throw new Exception("插入失败");
        }
        return null;
    }

    @Override
    public Integer PlusCommunication(Integer customerId, Integer communicationWay) throws Exception {
        int result = communicationLogDao.PlusCommunication(customerId, communicationWay);
        if (result < 1) {
            throw new Exception("更新沟通次数失败");
        }
        return null;
    }

    @Override
    public String editCommunicationLog(CommunicationLog communicationLog) throws Exception{
        int result = communicationLogDao.editCommunicationLog(communicationLog);
        if (result < 1) {
            throw new Exception("更新失败");
        }
        return null;
    }

    @Override
    public String deleteCommunicationLog(Integer id) throws Exception{
        int result = communicationLogDao.deleteCommunicationLog(id);
        if (result < 1) {
            throw new Exception("删除失败");
        }
        return null;
    }

    @Override
    public Integer MinusCommunication(Integer customerId, Integer communicationWay) throws Exception {
        int result = communicationLogDao.MinusCommunication(customerId, communicationWay);
        if (result < 1) {
            throw new Exception("更新沟通次数失败");
        }
        return null;
    }
}
