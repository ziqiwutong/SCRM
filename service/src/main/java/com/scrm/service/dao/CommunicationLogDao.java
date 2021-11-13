package com.scrm.service.dao;

import com.scrm.service.entity.Communication;
import com.scrm.service.entity.CommunicationLog;
import com.scrm.service.entity.UserAndCommunication;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommunicationLogDao {
    UserAndCommunication queryCommunicationUser(Integer id);

    Communication queryCommunication(Integer id);

    List<CommunicationLog> queryCommunicationLog(Integer id);

    Integer addCommunicationLog(CommunicationLog communicationLog);

    Integer PlusCommunication(Long communicationId, Integer communicationWay);

    Integer editCommunicationLog(CommunicationLog communicationLog);

    Integer deleteCommunicationLog(Integer id);

    Integer MinusCommunication(Integer communicationId, Integer communicationWay);
}
