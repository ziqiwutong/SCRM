package com.scrm.service.service.impl;

import com.scrm.service.dao.CommunicationLogDao;
import com.scrm.service.entity.Communication;
import com.scrm.service.entity.CommunicationLog;
import com.scrm.service.service.CommunicationLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommunicationLogServiceImpl implements CommunicationLogService {
    @Resource
    private CommunicationLogDao communicationLogDao;

    @Override
    public Communication queryCommunication(Integer id) {
        return communicationLogDao.queryCommunication(id);
    }

    @Override
    public List<CommunicationLog> queryCommunicationLog(Integer id) {
        return communicationLogDao.queryCommunicationLog(id);
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
}
