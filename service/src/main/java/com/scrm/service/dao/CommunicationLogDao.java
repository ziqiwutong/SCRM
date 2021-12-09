package com.scrm.service.dao;

import com.scrm.service.entity.Communication;
import com.scrm.service.entity.CommunicationLog;
import com.scrm.service.entity.UserAndCommunication;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommunicationLogDao {
    List<UserAndCommunication> queryCommunicationUser(Long customerId);

    Communication queryCommunication(Long customerId);

    List<CommunicationLog> queryCommunicationLog(Long customerId, Integer communicationWay);

    CommunicationLog queryCommunicationLogDetail(Long id);

    Integer addCommunicationLog(CommunicationLog communicationLog);

    Integer PlusCommunication(Long customerId, Integer communicationWay);

    Integer editCommunicationLog(CommunicationLog communicationLog);

    Integer deleteCommunicationLog(Long id);

    Integer MinusCommunication(Long customerId, Integer communicationWay);
}
