package com.scrm.service.service;

import com.scrm.service.entity.Communication;
import com.scrm.service.entity.UserAndCommunication;

import java.util.List;

public interface CommunicationService {
    List<UserAndCommunication> queryCommunication(Integer pageCount, Integer currentPage);

    Integer queryCount();

    List<UserAndCommunication> queryCommunicationByKey(String key);

    String addCommunication(Communication communication);

    String editCommunication(Communication communication);

    String deleteCommunication(Integer id);
}
