package com.scrm.marketing.controller;

import com.scrm.marketing.service.ProCusBpLogService;
import com.scrm.marketing.util.resp.CodeEum;
import com.scrm.marketing.util.resp.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author fzk
 * @date 2021-10-20 22:28
 * 客户产品购买浏览记录 Controller
 */
@RestController
@RequestMapping(path = "/mk/customer/statistics")
public class ProCusBpLogController {
    @Resource
    private ProCusBpLogService proCusBpLogService;

    /**
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
            result = proCusBpLogService.queryCusPurchase(pageNum, pageSize);
        }
        // 情况2：查询 特定客户 的 每个种类 购买情况
        else if (productTypeId == null) {
            result = proCusBpLogService.queryCusPurchase(customerId);
        }
        // 情况3：查询 特定客户 的 特定种类 购买情况
        else {
            result = proCusBpLogService.queryCusPurchase(customerId, productTypeId);
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
        return proCusBpLogService.queryCusBrowse(customerId, pageNum, pageSize);
    }
}
