package com.scrm.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scrm.service.dao.CustomerDao;
import com.scrm.service.dao.LabelDao;
import com.scrm.service.entity.Customer;
import com.scrm.service.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerDao customerDao;

    @Resource
    private LabelDao labelDao;

    @Override
    public List<Customer> query(Integer pageCount, Integer currentPage, QueryWrapper<Customer> wrapper) {
        int offset = (currentPage - 1) * pageCount;
        wrapper.last(" limit " + offset + "," + pageCount);
        List<Customer> customers = customerDao.selectList(wrapper);
        for (Customer customer: customers) {
            customer.setCustomerLabels(labelDao.queryByCustomerId(customer.getId()));
        }
        return customers;
    }

    @Override
    public Integer queryCount(QueryWrapper<Customer> wrapper) {
        return customerDao.selectCount(wrapper);
    }

    @Override
    public Customer queryById(Integer id) {
        return customerDao.selectById(id);
    }

    @Override
    public String insert(Customer customer) {
        int result = customerDao.insert(customer);
        if (result < 1) {
            return "插入失败";
        }
        return null;
    }

    @Override
    @Transactional
    public String insertBatch(List<Customer> customers) {
        int result = 0;
        for (Customer customer: customers) {
            result += customerDao.insert(customer);
        }
        if (result < customers.size()) {
            return "插入失败";
        }
        return null;
    }

    @Override
    public String update(Customer customer) {
        int result = customerDao.updateById(customer);
        if (result < 1) {
            return "更新失败";
        }
        return null;
    }

    @Override
    public String delete(Integer id) {
        int result = customerDao.deleteById(id);
        if (result < 1) {
            return "删除失败";
        }
        return null;
    }

    @Override
    public String deleteBatch(List<Integer> idList) {
        int result = customerDao.deleteBatchIds(idList);
        if (result < 1) {
            return "删除失败";
        }
        return null;
    }
}
