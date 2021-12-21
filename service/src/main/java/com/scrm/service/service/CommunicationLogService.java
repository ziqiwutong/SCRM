package com.scrm.service.service;

import com.scrm.service.entity.Communication;
import com.scrm.service.entity.CustomerRelation;
import com.scrm.service.entity.UserAndCommunication;

import java.util.List;

public interface CommunicationLogService {
    List<UserAndCommunication> queryCommunicationUser(Long customerId);

    List<CustomerRelation> querySingleCustomerRelation(Long customerId, String relationType);

    List<CustomerRelation> queryCustomerRelation(Long customerId);

    CustomerRelation queryCustomerRelationDetail(Long id);

    Communication queryCustomerRelationTimes(Long customerId);

    Integer addCustomerRelation(CustomerRelation customerRelation) throws Exception;

    Integer editCustomerRelation(CustomerRelation customerRelation) throws Exception;

    Integer deleteCustomerRelation(Long id) throws Exception;
}
