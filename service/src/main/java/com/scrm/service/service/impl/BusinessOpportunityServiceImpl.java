package com.scrm.service.service.impl;


import com.scrm.service.dao.BusinessOpportunityDao;
import com.scrm.service.entity.BusinessOpportunity;
import com.scrm.service.service.BusinessOpportunityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BusinessOpportunityServiceImpl implements BusinessOpportunityService {
    @Resource
    private BusinessOpportunityDao se_business_opportunityDao;

    @Override
    public List<BusinessOpportunity> queryBizOpportunity(Integer pageCount, Integer currentPage, String boStatus) {
        return se_business_opportunityDao.queryBizOpportunity(pageCount, currentPage, boStatus);
    }

    @Override
    public List<BusinessOpportunity> queryBizOppByKey(String key) {
        return se_business_opportunityDao.queryBizOppByKey(key);
    }

    @Override
    public BusinessOpportunity queryBizOpportunityDetail(Integer id) {
        return se_business_opportunityDao.queryBizOpportunityDetail(id);
    }

    @Override
    public Integer queryCount() {
        return se_business_opportunityDao.queryCount();
    }

    @Override
    @Transactional//开启事务
    public String addBizOpp(BusinessOpportunity se_business_opportunity) throws Exception {
        int result = se_business_opportunityDao.addBizOpp(se_business_opportunity);
        if (result < 1) {
            throw new Exception("插入失败");
        }
        return null;
    }

    @Override
    @Transactional//开启事务
    public String editBizOpp(BusinessOpportunity se_business_opportunity) throws Exception {
        int result = se_business_opportunityDao.editBizOpp(se_business_opportunity);
        if (result < 1) {
            throw new Exception("更新失败");
        }
        return null;
    }

    @Override
    @Transactional//开启事务
    public String deleteBizOpp(Integer id) throws Exception {
        int result = se_business_opportunityDao.deleteBizOpp(id);
        if (result < 1) {
            throw new Exception("删除失败");
        }
        return null;
    }
}
