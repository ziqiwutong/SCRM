package com.scrm.service.dao;

import com.scrm.service.entity.BusinessOpportunity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BusinessOpportunityDao {
    List<BusinessOpportunity> queryBizOpportunity(Integer pageCount, Integer currentPage);

    List<BusinessOpportunity> queryBizOppByKey(String key);

    Integer queryCount();

    Integer addBizOpp(BusinessOpportunity se_business_opportunity);

    Integer editBizOpp(BusinessOpportunity se_business_opportunity);

    Integer deleteBizOpp(Integer id);
}
