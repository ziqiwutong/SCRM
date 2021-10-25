package com.scrm.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scrm.service.dao.CustomerDao;
import com.scrm.service.entity.Customer;
import com.scrm.service.service.CustomerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerDao customerDao;

    @Override
    public List<Customer> query(Integer pageCount, Integer currentPage) {
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        int offset = (currentPage - 1) * pageCount;
        wrapper.last(" limit " + offset + "," + pageCount);
        return customerDao.selectList(wrapper);
    }

    @Override
    public Integer queryCount() {
        return customerDao.selectCount(null);
    }

    @Override
    public Customer queryById(Integer id) {
        return customerDao.selectById(id);
    }

    @Override
    public String insert(Customer customer) {
        int result = customerDao.insert(customer);
        if (result < 1) {
            return "更新失败";
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
}
