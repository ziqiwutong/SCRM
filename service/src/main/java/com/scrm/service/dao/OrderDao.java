package com.scrm.service.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author Ganyunhui
 * @create 2021-11-03 20:20
 */
@Mapper
public interface OrderDao {
    List<Map<Object, Object>> queryOrder(Integer pageNum, Integer pageSize, Integer orderType);

    Integer queryCount();

    Map<Object, Object> queryOrderDetail(String id);

    List<Map<Object, Object>> queryOrderByKey(String keySearch, Integer orderType);

    Integer deleteOrder(String orderID);

    Integer deleteOrderWith(String orderID);

    List<Map<Object, Object>> queryOrderByCustomerID(String customerID);



}
