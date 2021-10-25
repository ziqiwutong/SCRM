package com.scrm.service.service;

import com.scrm.service.entity.Communication;
import com.scrm.service.entity.CommunicationLog;

import java.util.List;

public interface CommunicationLogService {
    Communication queryCommunication(Integer id);

    List<CommunicationLog> queryCommunicationLog(Integer id);

    String addCommunicationLog(CommunicationLog communicationLog) throws Exception;

    String editCommunicationLog(CommunicationLog communicationLog) throws Exception;

    String deleteCommunicationLog(Integer id) throws Exception;
}
