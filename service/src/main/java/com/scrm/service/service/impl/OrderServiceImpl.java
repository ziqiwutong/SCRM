package com.scrm.service.service.impl;

import com.scrm.service.dao.OrderDao;
import com.scrm.service.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Ganyunhui
 * @create 2021-11-03 20:23
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderDao orderDao;

    @Override
    public List<Map<Object, Object>> queryOrder(Integer pageNum, Integer pageSize, Integer orderType) {
        return orderDao.queryOrder(pageNum, pageSize, orderType);
    }

    @Override
    public Map<Object, Object> queryOrderDetail(String id) {
        return orderDao.queryOrderDetail(id);
    }

    @Override
    public List<Map<Object, Object>> queryOrderByKey(String keySearch, Integer orderType) {
        return orderDao.queryOrderByKey(keySearch, orderType);
    }

    @Override
    public List<Map<Object, Object>> queryOrderByCustomerID(String customerID) {
        return orderDao.queryOrderByCustomerID(customerID);
    }

    @Override
    @Transactional
    public Integer deleteOrder(String orderID) throws Exception{
        int result = orderDao.deleteOrder(orderID);
        if (result < 1) {
            throw new Exception("删除失败");
        }
        return 0;
    }

    @Override
    @Transactional
    public Integer deleteOrderWith(String orderID) throws Exception{
        int result = orderDao.deleteOrderWith(orderID);
        if (result < 1) {
            throw new Exception("删除失败");
        }
        return 0;
    }



    @Override
//    @Transactional
    public Integer queryCount() {
        return orderDao.queryCount();
    }

}
