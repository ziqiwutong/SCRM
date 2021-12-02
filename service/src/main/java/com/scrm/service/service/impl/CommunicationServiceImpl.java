package com.scrm.service.service.impl;

import com.scrm.service.dao.CommunicationDao;
import com.scrm.service.entity.Communication;
import com.scrm.service.entity.UserAndCommunication;
import com.scrm.service.service.CommunicationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional//开启事务
    public String addCommunication(Communication communication) throws Exception{
        int result = communicationDao.addCommunication(communication);
        if (result < 1) {
            throw new Exception("插入失败");
        }
        return null;
    }

    @Override
    @Transactional//开启事务
    public String editCommunication(Communication communication) throws Exception{
        int result = communicationDao.editCommunication(communication);
        if (result < 1) {
            throw new Exception("更新失败");
        }
        return null;
    }

    @Override
    @Transactional//开启事务
    public String deleteCommunication(Integer customerId) throws Exception{
        int result = communicationDao.deleteCommunication(customerId);
        if (result < 1) {
            throw new Exception("删除失败");
        }
        return null;
    }

    @Override
    public Integer deleteCommunicationLog(Integer id) throws Exception {
        int result = communicationDao.deleteCommunicationLog(id);
        return null;
    }
}
