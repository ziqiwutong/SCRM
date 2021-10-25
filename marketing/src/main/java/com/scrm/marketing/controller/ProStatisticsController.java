package com.scrm.marketing.controller;

import com.scrm.marketing.service.ProStatisticsService;
import com.scrm.marketing.util.resp.CodeEum;
import com.scrm.marketing.util.resp.PageResult;
import com.scrm.marketing.util.resp.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author fzk
 * @date 2021-10-20 21:22
 * 产品统计 Controller
 */
@RestController
@RequestMapping(path = "/mk/product/statistics")
public class ProStatisticsController {
    @Resource
    private ProStatisticsService proStatisticsService;

    /**
     * 查询产品购买情况，直接查询product表
     *
     * @param typeId   为空，则分页查询所有购买情况；不为空，则查询此分类下所有产品购买情况
     * @param pageNum  页数
     * @param pageSize 页大小
     * @return 返回 可能是PageResult，也可能是Result
     */
    @GetMapping(path = "/purchase")
    public Result queryProPurchase(
            @RequestParam(name = "typeId", required = false) Long typeId,
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "pageSize", required = false) Integer pageSize
    ) {
        // 1.参数检查：如果typeId为null，则必须传入分页参数
        if (typeId == null)
            if (pageNum == null || pageNum < 1 || pageSize == null || pageSize < 1)
                return PageResult.error(CodeEum.PARAM_ERROR);

        // 2.调用service
        return proStatisticsService.queryProPurchase(typeId, pageNum, pageSize);
    }

    /**
     * 查询产品浏览情况，查询mk_product_customer_bp_log表
     * 根据 productTypeId 的有无，判断是查询所有还是查询某类产品
     * 情况1：查询所有产品类型的总浏览时长
     * 情况2：查询某类产品下所有产品的浏览时长
     * @param productTypeId 产品类型id
     * @return 返回Result或者PageResult
     */
    @GetMapping(path = "/browse")
    public Result queryProBrowse(
            @RequestParam(name = "productTypeId", required = false) Long productTypeId) {
        // 调用service
        return proStatisticsService.queryProBrowse(productTypeId);
    }
}
