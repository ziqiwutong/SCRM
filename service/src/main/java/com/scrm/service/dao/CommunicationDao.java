package com.scrm.service.dao;

import com.scrm.service.entity.Communication;
import com.scrm.service.entity.UserAndCommunication;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommunicationDao {
    List<UserAndCommunication> queryCommunication(Integer pageCount, Integer currentPage);

    Integer queryCount();

    List<UserAndCommunication> queryCommunicationByKey(String key);

    Integer addCommunication(Communication communication);

    Integer editCommunication(Communication communication);

    Integer deleteCommunication(Integer id);
}
