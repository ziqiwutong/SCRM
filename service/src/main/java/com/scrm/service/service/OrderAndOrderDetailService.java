package com.scrm.service.service;

import com.scrm.service.entity.OrderAndOrderDetail;

import java.util.List;
import java.util.Map;

/**
 * @author Ganyunhui
 * @create 2021-10-20 21:49
 */
public interface OrderAndOrderDetailService {
    Map<Object,Object> queryOrderAndOrderDetail(String id);

    List<Map<Object,Object> > queryOrder(Integer pageNum, Integer pageSize, Integer orderType);

    Integer queryCount();

    List<Map<Object, Object> > queryOrderByKey(String keySearch, Integer orderType);

    Integer deleteOrder(String orderID) throws Exception;

    Integer deleteOrderWith(String orderID) throws Exception;

    Integer addOrder(OrderAndOrderDetail orderAndOrderDetail) throws Exception;

    Integer addOrderWith(OrderAndOrderDetail orderAndOrderDetail) throws Exception;

    Integer editOrder(OrderAndOrderDetail orderAndOrderDetail) throws Exception;

    Integer editOrderWith(OrderAndOrderDetail orderAndOrderDetail) throws Exception;



}
