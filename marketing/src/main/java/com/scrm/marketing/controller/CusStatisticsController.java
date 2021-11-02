package com.scrm.marketing.controller;

import com.scrm.marketing.service.CusStatisticsService;
import com.scrm.marketing.util.resp.CodeEum;
import com.scrm.marketing.util.resp.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;
import org.w3c.dom.html.HTMLDocument;

import javax.annotation.Resource;

/**
 * @author fzk
 * @date 2021-10-22 16:51
 * 对于客户统计分析的controller
 */
@RestController
@RequestMapping(path = "/mk/customer/statistics")
public class CusStatisticsController {
    @Resource
    private CusStatisticsService cusStatisticsService;

    /**
     * 查询客户阅读情况
     * 情况1：无customerId，分页查询客户阅读总时长
     * 情况2：有customerId，sevenFlag为true，查1周内客户每天阅读时长
     * 情况3：有customerId，sevenFlag为false，查1个月内客户每天阅读时长
     *
     * @param customerId 客户id optional
     * @param sevenFlag  7天标记 optional
     * @param pageNum    页码 optional
     * @param pageSize   页大小 optional
     * @return 返回Result或PageResult
     */
    @GetMapping(path = "/read")
    public Result queryCusRead(
            @RequestParam(name = "customerId", required = false) Long customerId,
            @RequestParam(name = "sevenFlag", required = false) Boolean sevenFlag,
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "pageSize", required = false) Integer pageSize
    ) {
        // 1.参数检查
        if (customerId == null) {
            if (pageNum == null || pageSize == null)
                return Result.error(CodeEum.PARAM_MISS);
            else if (pageNum < 1 || pageSize < 1)
                return Result.error(CodeEum.PARAM_ERROR);
        } else if (sevenFlag == null) return Result.error(CodeEum.PARAM_MISS);

        // 调用service
        return cusStatisticsService.queryCusRead(customerId, sevenFlag, pageNum, pageSize);
    }


    /**
     * 查询客户购买情况
     * 根据参数的有无，作出3种反应
     *
     * @param customerId    客户id optional
     * @param productTypeId 产品分类id optional
     * @param pageNum       页码 optional
     * @param pageSize      页大小 optional
     * @return 根据参数不同返回PageResult或者Result
     */
    @GetMapping(path = "/purchase")
    public Result queryCusPurchase(
            @RequestParam(name = "customerId", required = false) Long customerId,
            @RequestParam(name = "productTypeId", required = false) Long productTypeId,
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "pageSize", required = false) Integer pageSize
    ) {
        Result result;
        // 情况1：分页查询所有客户购买情况
        if (customerId == null) {
            // 参数检查
            if (pageNum == null || pageNum < 1 || pageSize == null || pageSize < 1)
                return Result.error(CodeEum.PARAM_ERROR);
            // 调用service
            result = cusStatisticsService.queryCusPurchase(pageNum, pageSize);
        }
        // 情况2：查询 特定客户 的 每个种类 购买情况
        else if (productTypeId == null) {
            result = cusStatisticsService.queryCusPurchase(customerId);
        }
        // 情况3：查询 特定客户 的 特定种类 购买情况
        else {
            result = cusStatisticsService.queryCusPurchase(customerId, productTypeId);
        }
        return result;
    }

    /**
     * 查询客户浏览产品总时长
     * 情况1：分页查询客户浏览总时长
     * 情况2：查询某个客户浏览每个产品分类总时长
     *
     * @param customerId 客户id optional
     * @param pageNum    页码 optional
     * @param pageSize   页大小 optional
     * @return 返回Result或者PageResult
     */
    @GetMapping("/browse")
    public Result queryCusBrowse(
            @RequestParam(name = "customerId", required = false) Long customerId,
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "pageSize", required = false) Integer pageSize
    ) {
        // 1.参数检查：customerId为null，则pageNum和pageSize必须有值
        if (customerId == null) {
            if (pageNum == null || pageSize == null)
                return Result.error(CodeEum.PARAM_MISS);
            else if (pageNum < 1 || pageSize < 1)
                return Result.error(CodeEum.PARAM_ERROR);
        }
        // 调用service
        return cusStatisticsService.queryCusBrowse(customerId, pageNum, pageSize);
    }
}
