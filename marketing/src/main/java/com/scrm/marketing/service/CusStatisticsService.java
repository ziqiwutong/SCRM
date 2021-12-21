package com.scrm.marketing.service;

import com.scrm.marketing.util.resp.PageResult;
import com.scrm.marketing.util.resp.Result;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author fzk
 * @date 2021-10-22 17:01
 */
public interface CusStatisticsService {
    Result queryCusRead(Long customerId, Boolean sevenFlag, Integer pageNum, Integer pageSize);

    /**
     * 分页查询所有客户购买情况
     *
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return 返回PageResult
     */
    PageResult queryCusPurchase(Integer pageNum, Integer pageSize);

    /**
     * 查询 特定客户 购买的 所有种类 购买情况
     *
     * @param customerId 客户id
     * @return 返回Result
     */
    Result queryCusPurchase(Long customerId);

    /**
     * 查询 特定客户 购买的 特定种类 购买情况
     *
     * @param customerId    客户id
     * @param productTypeId 产品分类id
     * @return 返回Result
     */
    Result queryCusPurchase(Long customerId, Long productTypeId);

    Result queryCusBrowse(Long customerId, Integer pageNum, Integer pageSize);
}
