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
    Map<Object, Object> queryOrderAndOrderDetail(Long id);

    List<Map<Object, Object>> queryOrder(Integer pageNum, Integer pageSize, Integer orderType);

    Integer queryCount();

    List<Map<Object, Object>> queryOrderByKey(String keySearch, Integer orderType);

    Integer deleteOrder(Integer orderID);

    Integer deleteOrderWith(Integer orderID);

    Integer addOrder(OrderAndOrderDetail orderAndOrderDetail);

    Integer addOrderWith(OrderAndOrderDetail orderAndOrderDetail);
}

