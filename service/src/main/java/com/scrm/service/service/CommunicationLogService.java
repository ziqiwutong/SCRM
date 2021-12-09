package com.scrm.service.service;

import com.scrm.service.entity.Communication;
import com.scrm.service.entity.CommunicationLog;
import com.scrm.service.entity.UserAndCommunication;

import java.util.List;

public interface CommunicationLogService {
    List<UserAndCommunication> queryCommunicationUser(Long customerId);

    Communication queryCommunication(Long customerId);

    List<CommunicationLog> queryCommunicationLog(Long customerId, Integer communicationWay);

    CommunicationLog queryCommunicationLogDetail(Long id);

    String addCommunicationLog(CommunicationLog communicationLog) throws Exception;

    Integer PlusCommunication(Long customerId, Integer communicationWay) throws Exception;

    String editCommunicationLog(CommunicationLog communicationLog) throws Exception;

    String deleteCommunicationLog(Long id) throws Exception;

    Integer MinusCommunication(Long customerId, Integer communicationWay) throws Exception;
}
