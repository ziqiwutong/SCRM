package com.scrm.service.service;

import com.scrm.service.entity.Product;

import java.util.List;

/**
 * 微盟对接
 */
public interface WeimobService {

    /**
     * 查询产品
     * @param pageCount 每页数量
     * @param currentPage 当前页数
     * @return List<Product>
     */
    List<Product> queryProduct(Integer pageCount, Integer currentPage);

    /**
     * 查询产品数量
     * @return Integer
     */
    Integer queryProductCount();

    /**
     * 同步产品
     * @return String
     */
    String syncProduct();

    /**
     * 同步昨天的订单
     * @return String
     */
    String syncOrder();

    /**
     * 获取用户微盟分销链接
     * @param userId 用户ID
     * @return String
     */
    String distributeUrl(Long userId);
}
