package com.scrm.service.service;

import com.scrm.service.entity.Communication;
import com.scrm.service.entity.CommunicationLog;

import java.util.List;

public interface CommunicationLogService {
    Communication queryCommunication(Integer id);

    List<CommunicationLog> queryCommunicationLog(Integer id);

    String addCommunicationLog(CommunicationLog communicationLog);

    String editCommunicationLog(CommunicationLog communicationLog);

    String deleteCommunicationLog(Integer id);
}
