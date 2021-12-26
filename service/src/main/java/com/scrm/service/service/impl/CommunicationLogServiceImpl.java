package com.scrm.service.service.impl;

import com.scrm.service.dao.CommunicationLogDao;
import com.scrm.service.entity.Communication;
import com.scrm.service.entity.CustomerRelation;
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
    public List<UserAndCommunication> queryCommunicationUser(Long customerId) {
        return communicationLogDao.queryCommunicationUser(customerId);
    }

    @Override
    public List<CustomerRelation> querySingleCustomerRelation(Long customerId, String relationType) {
        return communicationLogDao.querySingleCustomerRelation(customerId, relationType);
    }

    @Override
    public List<CustomerRelation> queryCustomerRelation(Long customerId) {
        return communicationLogDao.queryCustomerRelation(customerId);
    }

    @Override
    public CustomerRelation queryCustomerRelationDetail(Long id) {
        return communicationLogDao.queryCustomerRelationDetail(id);
    }

    @Override
    public Communication queryCustomerRelationTimes(Long customerId) {
        int callTimes = communicationLogDao.queryCustomerRelationCount(customerId,"打电话");
        int msgTimes = communicationLogDao.queryCustomerRelationCount(customerId,"发短信");
        int wxTimes = communicationLogDao.queryCustomerRelationCount(customerId,"发微信");
        int visitTimes = communicationLogDao.queryCustomerRelationCount(customerId,"线下沟通");
        Communication communication = new Communication();
        communication.setCallTimes(callTimes);
        communication.setMsgTimes(msgTimes);
        communication.setWxTimes(wxTimes);
        communication.setVisitTimes(visitTimes);
        return communication;
    }

    @Override
    public Integer addCustomerRelation(CustomerRelation customerRelation) throws Exception {
        int result = communicationLogDao.addCustomerRelation(customerRelation);
        if (result < 1) {
            throw new Exception("插入失败");
        }
        return null;
    }

    @Override
    public Integer editCustomerRelation(CustomerRelation customerRelation) throws Exception {
        int result = communicationLogDao.editCustomerRelation(customerRelation);
        if (result < 1) {
            throw new Exception("修改失败");
        }
        return null;
    }

    @Override
    public Integer deleteCustomerRelation(Long id) throws Exception {
        int result = communicationLogDao.deleteCustomerRelation(id);
        if (result < 1) {
            throw new Exception("删除失败");
        }
        return null;
    }
}
