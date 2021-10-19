package com.scrm.service.service.impl;

import com.scrm.service.dao.CommunicationDao;
import com.scrm.service.entity.Communication;
import com.scrm.service.entity.UserAndCommunication;
import com.scrm.service.service.CommunicationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommunicationServiceImpl implements CommunicationService {
    @Resource
    private CommunicationDao communicationDao;

    @Override
    public List<UserAndCommunication> queryCommunication(Integer pageCount, Integer currentPage) {
        return communicationDao.queryCommunication(pageCount,currentPage);
    }

    @Override
    public Integer queryCount() { return communicationDao.queryCount(); }

    @Override
    public List<UserAndCommunication> queryCommunicationByKey(String key) {
        return communicationDao.queryCommunicationByKey(key);
    }

    @Override
    public String addCommunication(Communication communication) {
        int result = communicationDao.addCommunication(communication);
        if (result < 1) {
            return "插入失败";
        }
        return null;
    }

    @Override
    public String editCommunication(Communication communication) {
        int result = communicationDao.editCommunication(communication);
        if (result < 1) {
            return "更新失败";
        }
        return null;
    }

    @Override
    public String deleteCommunication(Integer id) {
        int result = communicationDao.deleteCommunication(id);
        if (result < 1) {
            return "删除失败";
        }
        return null;
    }
}
