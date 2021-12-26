package com.scrm.service.dao;

import com.scrm.service.entity.CustomerRelation;
import com.scrm.service.entity.UserAndCommunication;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommunicationLogDao {
    List<UserAndCommunication> queryCommunicationUser(Long customerId);

    List<CustomerRelation> querySingleCustomerRelation(Long customerId, String relationType);

    List<CustomerRelation> queryCustomerRelation(Long customerId);

    CustomerRelation queryCustomerRelationDetail(Long id);

    Integer queryCustomerRelationCount(Long customerId, String relationType);

    Integer addCustomerRelation(CustomerRelation customerRelation);

    Integer editCustomerRelation(CustomerRelation customerRelation);

    Integer deleteCustomerRelation(Long id);
}
