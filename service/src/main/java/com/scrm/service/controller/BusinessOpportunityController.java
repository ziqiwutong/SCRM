package com.scrm.service.controller;

import com.scrm.service.entity.BusinessOpportunity;
import com.scrm.service.entity.Clue;
import com.scrm.service.service.BusinessOpportunityService;
import com.scrm.service.util.resp.PageResp;
import com.scrm.service.util.resp.Resp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@RequestMapping(value = "/businessOpportunity")
@RestController
public class BusinessOpportunityController {
    @Resource
    private BusinessOpportunityService se_business_opportunityService;

    @GetMapping(value = "/queryBizOpportunity")
    @ResponseBody
    public PageResp queryBizOpportunity(
            @RequestParam(value = "pageCount", required = false) Integer pageCount,
            @RequestParam(value = "currentPage", required = false) Integer currentPage) {
        return PageResp.success().setData(
                se_business_opportunityService.queryBizOpportunity(pageCount, currentPage)
        ).setPage(pageCount, currentPage, se_business_opportunityService.queryCount()).setMsg("成功");
    }

    @GetMapping(value = "/queryBizOppByKey")
    @ResponseBody
    public Resp queryBizOppByKey(
            @RequestParam(value = "keySearch") String keySearch
    ) {
        List<BusinessOpportunity> se_business_opportunity = se_business_opportunityService.queryBizOppByKey(keySearch);
        if (se_business_opportunity.size() == 0) {
            return Resp.error().setMsg("无数据");
        } else {
            return Resp.success().setData(
                    se_business_opportunity
            ).setMsg("成功");
        }
    }

    @PostMapping(value = "/addBizOpp")
    @ResponseBody
    public Resp addBizOpp(
            BusinessOpportunity businessOpportunity
    ) {
        if (businessOpportunity == null) {
            return Resp.error().setMsg("不能为空");
        }
        try {
            String result = se_business_opportunityService.addBizOpp(businessOpportunity);
            return Resp.success().setMsg("插入成功");
        } catch (Exception e) {
            return Resp.error().setMsg(e.getMessage());
        }
    }

    @PostMapping(value = "/editBizOpp")
    @ResponseBody
    public Resp editBizOpp(
            BusinessOpportunity businessOpportunity
    ) {
        if (businessOpportunity == null) {
            return Resp.error().setMsg("不能为空");
        }
        try {
            String result = se_business_opportunityService.editBizOpp(businessOpportunity);
            return Resp.success().setMsg("更新成功");
        } catch (Exception e) {
            return Resp.error().setMsg(e.getMessage());
        }
    }

    @PostMapping(value = "/deleteBizOpp")
    @ResponseBody
    public Resp deleteBizOpp(
            @RequestParam(value = "id") Integer id
    ) {
        try {
            String result = se_business_opportunityService.deleteBizOpp(id);
            return Resp.success().setMsg("删除成功");
        } catch (Exception e) {
            return Resp.error().setMsg(e.getMessage());
        }
    }
}
