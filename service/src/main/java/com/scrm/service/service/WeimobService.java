package com.scrm.service.service;

/**
 * 微盟对接
 */
public interface WeimobService {

    /**
     * 同步产品
     * @return String
     */
    String queryProduct();

    /**
     * 同步昨天的订单
     * @return String
     */
    String queryOrder();
}
