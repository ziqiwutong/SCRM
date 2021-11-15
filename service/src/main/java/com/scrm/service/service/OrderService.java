package com.scrm.service.service;

import java.util.List;
import java.util.Map;

/**
 * @author Ganyunhui
 * @create 2021-11-03 20:22
 */
public interface OrderService {
    Integer queryCount();

    List<Map<Object, Object>> queryOrder(Integer pageNum, Integer pageSize, Integer orderType);

    Map<Object, Object> queryOrderDetail(String id);

    List<Map<Object, Object>> queryOrderByKey(String keySearch, Integer orderType);

    Integer deleteOrder(String orderID) throws Exception;

    Integer deleteOrderWith(String orderID) throws Exception;

    List<Map<Object, Object>> queryOrderByCustomerID(String customerID);



}
