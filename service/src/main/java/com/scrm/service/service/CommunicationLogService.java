package com.scrm.service.service;

import com.scrm.service.entity.Communication;
import com.scrm.service.entity.CommunicationLog;
import com.scrm.service.entity.UserAndCommunication;

import java.util.List;

public interface CommunicationLogService {
    UserAndCommunication queryCommunicationUser(Integer id);

    Communication queryCommunication(Integer id);

    List<CommunicationLog> queryCommunicationLog(Integer id);

    String addCommunicationLog(CommunicationLog communicationLog) throws Exception;

    Integer PlusCommunication(Long communicationId, Integer communicationWay) throws Exception;

    String editCommunicationLog(CommunicationLog communicationLog) throws Exception;

    String deleteCommunicationLog(Integer id) throws Exception;

    Integer MinusCommunication(Integer communicationId, Integer communicationWay) throws Exception;
}
