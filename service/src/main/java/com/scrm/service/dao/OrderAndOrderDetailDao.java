package com.scrm.service.dao;

import com.scrm.service.entity.OrderAndOrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author Ganyunhui
 * @create 2021-10-20 21:28
 */
@Mapper
public interface OrderAndOrderDetailDao {
    Map<Object, Object> queryOrderAndOrderDetail(String id);

    List<Map<Object, Object>> queryOrder(Integer pageNum, Integer pageSize, Integer orderType);

    Integer queryCount();

    List<Map<Object, Object>> queryOrderByKey(String keySearch, Integer orderType);

    Integer deleteOrder(String orderID);

    Integer deleteOrderWith(String orderID);

    Integer addOrder(OrderAndOrderDetail orderAndOrderDetail);

    Integer addOrderWith(OrderAndOrderDetail orderAndOrderDetail);

    Integer editOrder(OrderAndOrderDetail orderAndOrderDetail);

    Integer editOrderWith(OrderAndOrderDetail orderAndOrderDetail);
}

