package com.scrm.service.service.impl;

import com.scrm.service.dao.OrderAndOrderDetailDao;
import com.scrm.service.entity.OrderAndOrderDetail;
import com.scrm.service.service.OrderAndOrderDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Ganyunhui
 * @create 2021-10-20 21:51
 */
@Service
public class OrderAndOrderDetailServiceImpl implements OrderAndOrderDetailService {
    @Resource
    private OrderAndOrderDetailDao orderAndOrderDetailDao;


    @Override
    public Map<Object,Object> queryOrderAndOrderDetail(Long id) {
        return orderAndOrderDetailDao.queryOrderAndOrderDetail(id);
    }

    @Override
    public List<Map<Object,Object> > queryOrder(Integer pageNum, Integer pageSize,  Integer orderType) {
        return orderAndOrderDetailDao.queryOrder(pageNum, pageSize, orderType);
    }

    @Override
//    @Transactional
    public Integer queryCount() {
        return orderAndOrderDetailDao.queryCount();
    }

    @Override
    public List<Map<Object, Object>> queryOrderByKey(String keySearch, Integer orderType) {
        return orderAndOrderDetailDao.queryOrderByKey(keySearch, orderType);
    }

    @Override
    @Transactional
    public Integer deleteOrder(Integer orderID) throws Exception {
        int result = orderAndOrderDetailDao.deleteOrder(orderID);
        if (result < 1) {
            throw new Exception("删除失败");
        }
        return 0;
    }

    @Override
    public Integer deleteOrderWith(Integer orderID) throws Exception {
        int result = orderAndOrderDetailDao.deleteOrderWith(orderID);
        if (result < 1) {
            throw new Exception("删除失败");
        }
        return 0;
    }

    @Override
    @Transactional//开启事务
    public Integer addOrder(OrderAndOrderDetail orderAndOrderDetail) throws Exception {
        int result = orderAndOrderDetailDao.addOrder(orderAndOrderDetail);
        if (result < 1) {
            throw new Exception("插入失败");
        }
        return 0;
    }

    @Override
    @Transactional//开启事务
    public Integer addOrderWith(OrderAndOrderDetail orderAndOrderDetail) throws Exception {
        int result = orderAndOrderDetailDao.addOrderWith(orderAndOrderDetail);
        if (result < 1) {
            throw new Exception("插入失败");
        }
        return 0;
    }


}
