package com.scrm.service.dao;

import com.scrm.service.entity.Communication;
import com.scrm.service.entity.CommunicationLog;
import com.scrm.service.entity.UserAndCommunication;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommunicationLogDao {
    List<UserAndCommunication> queryCommunicationUser(Integer customerId);

    Communication queryCommunication(Integer customerId);

    List<CommunicationLog> queryCommunicationLog(Integer id, Integer customerId, Integer communicationWay);

    Integer addCommunicationLog(CommunicationLog communicationLog);

    Integer PlusCommunication(Integer customerId, Integer communicationWay);

    Integer editCommunicationLog(CommunicationLog communicationLog);

    Integer deleteCommunicationLog(Integer id);

    Integer MinusCommunication(Integer customerId, Integer communicationWay);
}
