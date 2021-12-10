package com.scrm.service.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scrm.service.entity.Order;

import java.util.List;

/**
 * @author Ganyunhui
 * @create 2021-11-03 20:22
 */
public interface OrderService {

    List<Order> queryPage(Integer pageCount, Integer currentPage, QueryWrapper<Order> wrapper);

    int queryCount(QueryWrapper<Order> wrapper);

    Order queryByOrderNum(String num);

    String insert(Order order);

    String update(Order order);

    String delete(Long id);
}
