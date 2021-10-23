package com.scrm.service.controller;


import com.scrm.service.entity.BusinessOpportunity;
import com.scrm.service.service.BusinessOpportunityService;
import com.scrm.service.util.resp.CodeEum;
import com.scrm.service.util.resp.PageResult;
import com.scrm.service.util.resp.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping(value = "/businessOpportunity")
@RestController
public class BusinessOpportunityController {
    @Resource
    private BusinessOpportunityService se_business_opportunityService;

    @GetMapping(value = "/queryBizOpportunity")
    public PageResult queryBizOpportunity(
            @RequestParam(value = "pageCount", required = false) Integer pageCount,
            @RequestParam(value = "currentPage", required = false) Integer currentPage) {

        // 查询数据
        List<BusinessOpportunity> businessOpportunities =
                se_business_opportunityService.queryBizOpportunity(pageCount, currentPage);
        // 查询总的条数
        Integer count = se_business_opportunityService.queryCount();
        return PageResult.success(businessOpportunities, count, currentPage);
    }

    @GetMapping(value = "/queryBizOppByKey")
    public Result queryBizOppByKey(
            @RequestParam(value = "keySearch") String keySearch
    ) {
        List<BusinessOpportunity> se_business_opportunity = se_business_opportunityService.queryBizOppByKey(keySearch);
        return Result.success(se_business_opportunity);
    }

    @PostMapping(value = "/addBizOpp")
    public Result addBizOpp(
            BusinessOpportunity businessOpportunity
    ) {
        if (businessOpportunity == null) {
           return Result.error(CodeEum.PARAM_MISS);
        }
        try {
            se_business_opportunityService.addBizOpp(businessOpportunity);
            return Result.success();
        } catch (Exception e) {
            return Result.error(CodeEum.FAIL);
        }
    }

    @PostMapping(value = "/editBizOpp")
    public Result editBizOpp(
            BusinessOpportunity businessOpportunity
    ) {
        if (businessOpportunity == null) {
            return Result.error(CodeEum.PARAM_MISS);
        }
        try {
            se_business_opportunityService.editBizOpp(businessOpportunity);
            return Result.success();
        } catch (Exception e) {
            return Result.error(CodeEum.FAIL);
        }
    }

    @PostMapping(value = "/deleteBizOpp")
    public Result deleteBizOpp(
            @RequestParam(value = "id") Integer id
    ) {
        try {
            se_business_opportunityService.deleteBizOpp(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(CodeEum.FAIL);
        }
    }
}
