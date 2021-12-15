package com.scrm.service.service;

import com.scrm.service.entity.Communication;
import com.scrm.service.entity.UserAndCommunication;

import java.util.List;

public interface CommunicationService {
    List<UserAndCommunication> queryCommunication(Integer pageCount, Integer currentPage);

    Integer queryCount();

    List<UserAndCommunication> queryCommunicationByKey(String key);

    String addCommunication(Communication communication) throws Exception;

    String editCommunication(Communication communication) throws Exception;

    String deleteCommunication(Long id) throws Exception;

    Integer deleteCommunicationLog(Long id) throws Exception;
}
