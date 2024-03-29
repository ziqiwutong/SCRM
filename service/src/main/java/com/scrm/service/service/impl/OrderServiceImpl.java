package com.scrm.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scrm.service.dao.OrderDao;
import com.scrm.service.dao.OrderProductDao;
import com.scrm.service.entity.Order;
import com.scrm.service.entity.OrderProduct;
import com.scrm.service.service.OrderService;
import com.scrm.service.util.order.GenerateNum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Ganyunhui
 * @create 2021-11-03 20:23
 */
@Service
public class OrderServiceImpl implements OrderService {
    
    @Resource
    private OrderDao orderDao;

    @Resource
    private OrderProductDao orderProductDao;

    @Resource
    private GenerateNum generateNum;

    @Override
    @Transactional
    public List<Order> queryPage(Integer pageCount, Integer currentPage, QueryWrapper<Order> wrapper) {
        int offset = (currentPage - 1) * pageCount;
        wrapper.last(" limit " + offset + "," + pageCount);
        List<Order> list = orderDao.selectList(wrapper);
        for (Order order : list) {
            queryOrderProduct(order);
        }
        return list;
    }

    @Override
    public int queryCount(QueryWrapper<Order> wrapper) {
        return orderDao.selectCount(wrapper);
    }

    @Override
    public Order queryByOrderNum(String num) {
        List<Order> list = orderDao.queryByOrderNum(num);
        if (list.size() == 0) return null;
        Order order = list.get(0);
        queryOrderProduct(order);
        return order;
    }

    @Override
    @Transactional
    public String insert(Order order) {
        if (order.getOrderNum() == null || order.getOrderNum().isEmpty()) {
            order.setOrderNum(generateNum.GenerateOrder());
        }
        int result = orderDao.insert(order);
        if (order.getProductList() != null) {
            insertOrderProduct(order);
        }
        if (result < 1) {
            return "插入失败";
        }
        return null;
    }

    @Override
    @Transactional
    public String update(Order order) {
        if (order.getOrderNum() != null) order.setOrderNum(null);
        int result = orderDao.updateById(order);
        QueryWrapper<OrderProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", order.getId());
        orderProductDao.delete(queryWrapper);
        if (order.getProductList() != null) {
            insertOrderProduct(order);
        }
        if (result < 1) {
            return "更新失败";
        }
        return null;
    }

    @Override
    @Transactional
    public String delete(Long id) {
        Order order = orderDao.selectById(id);
        if (order == null) return "订单不存在";
        String orderNum = order.getOrderNum();
        if (orderNum != null && orderNum.length() > 3 && orderNum.charAt(2) == '_') return "外部订单禁止删除";
        int result = orderDao.deleteById(id);
        if (result < 1) return "删除失败";
        QueryWrapper<OrderProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", id);
        orderProductDao.delete(wrapper);
        return null;
    }

    @Override
    public List<Long> queryIdByProduct(String keyword) {
        return orderDao.queryIdByProduct(keyword);
    }

    /**
     * 查询订单下的产品信息
     */
    private void queryOrderProduct(Order order) {
        QueryWrapper<OrderProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", order.getId());
        order.setProductList(orderProductDao.selectList(wrapper));
        order.setProductCount(orderProductDao.queryCountByOrder(order.getId()));
    }

    /**
     * 插入订单下的产品信息
     */
    private void insertOrderProduct(Order order) {
        for (OrderProduct orderProduct : order.getProductList()) {
            orderProduct.setOrderId(order.getId());
            orderProductDao.insert(orderProduct);
        }
    }
}
