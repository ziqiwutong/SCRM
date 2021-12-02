package com.scrm.service.service;

import com.scrm.service.entity.BusinessOpportunity;

import java.util.List;

public interface BusinessOpportunityService {
    List<BusinessOpportunity> queryBizOpportunity(Integer pageCount, Integer currentPage, String boStatus);

    List<BusinessOpportunity> queryBizOppByKey(String key);

    BusinessOpportunity queryBizOpportunityDetail(Integer id);

    Integer queryCount();

    String addBizOpp(BusinessOpportunity se_business_opportunity) throws Exception;

    String editBizOpp(BusinessOpportunity se_business_opportunity) throws Exception;

    String deleteBizOpp(Integer id) throws Exception;
}
