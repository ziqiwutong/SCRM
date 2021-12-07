package com.scrm.service.service;

import com.scrm.service.entity.Communication;
import com.scrm.service.entity.CommunicationLog;
import com.scrm.service.entity.UserAndCommunication;

import java.util.List;

public interface CommunicationLogService {
    List<UserAndCommunication> queryCommunicationUser(Integer customerId);

    Communication queryCommunication(Integer customerId);

    List<CommunicationLog> queryCommunicationLog(Integer customerId, Integer communicationWay);

    CommunicationLog queryCommunicationLogDetail(Integer id);

    String addCommunicationLog(CommunicationLog communicationLog) throws Exception;

    Integer PlusCommunication(Integer customerId, Integer communicationWay) throws Exception;

    String editCommunicationLog(CommunicationLog communicationLog) throws Exception;

    String deleteCommunicationLog(Integer id) throws Exception;

    Integer MinusCommunication(Integer customerId, Integer communicationWay) throws Exception;
}
