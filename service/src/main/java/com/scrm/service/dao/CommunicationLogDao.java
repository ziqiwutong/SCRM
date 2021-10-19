package com.scrm.service.dao;

import com.scrm.service.entity.Communication;
import com.scrm.service.entity.CommunicationLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommunicationLogDao {
    Communication queryCommunication(Integer id);

    List<CommunicationLog> queryCommunicationLog(Integer id);

    Integer addCommunicationLog(CommunicationLog communicationLog);

    Integer editCommunicationLog(CommunicationLog communicationLog);

    Integer deleteCommunicationLog(Integer id);
}
