package com.scrm.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scrm.service.dao.CustomerCustomizedFieldDao;
import com.scrm.service.dao.CustomerDao;
import com.scrm.service.dao.CustomerRelationDao;
import com.scrm.service.dao.LabelDao;
import com.scrm.service.entity.Customer;
import com.scrm.service.entity.CustomerCustomizedField;
import com.scrm.service.entity.CustomerRelation;
import com.scrm.service.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerDao customerDao;

    @Resource
    private CustomerCustomizedFieldDao customerCustomizedFieldDao;

    @Resource
    private CustomerRelationDao customerRelationDao;

    @Resource
    private LabelDao labelDao;

    @Override
    public List<Customer> query(QueryWrapper<Customer> wrapper) {
        List<Customer> customers = customerDao.selectList(wrapper);
        for (Customer customer : customers) {
            customer.setCustomerLabels(labelDao.queryByCustomerId(customer.getId()));
        }
        return customers;
    }

    @Override
    public List<Customer> queryPage(Integer pageCount, Integer currentPage, QueryWrapper<Customer> wrapper) {
        int offset = (currentPage - 1) * pageCount;
        wrapper.last(" limit " + offset + "," + pageCount);
        List<Customer> customers = customerDao.selectList(wrapper);
        for (Customer customer : customers) {
            customer.setCustomerLabels(labelDao.queryByCustomerId(customer.getId()));
        }
        return customers;
    }

    @Override
    public List<Long> queryIdByBusinessTime(String start, String end) {
        return customerDao.queryIdByBusinessTime(start, end);
    }

    @Override
    public Integer queryCount(QueryWrapper<Customer> wrapper) {
        return customerDao.selectCount(wrapper);
    }

    @Override
    public Customer queryById(Long id) {
        Customer customer = customerDao.selectById(id);
        customer.setCustomerLabels(labelDao.queryByCustomerId(customer.getId()));
        return customer;
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
        for (Customer customer : customers) {
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
    public String delete(Long id) {
        int result = customerDao.deleteById(id);
        if (result < 1) {
            return "删除失败";
        }
        return null;
    }

    @Override
    public String deleteBatch(List<Long> idList) {
        int result = customerDao.deleteBatchIds(idList);
        if (result < 1) {
            return "删除失败";
        }
        return null;
    }

    @Override
    public List<CustomerCustomizedField> queryCustomizedField() {
        return customerCustomizedFieldDao.selectList(null);
    }

    @Override
    @Transactional
    public String label(HashSet<Long> customerIds, List<Long> labelIds) {
        for (Long id : customerIds) {
            List<Long> old = labelDao.queryLabelIdByCustomerId(id);
            ArrayList<Long> delete = new ArrayList<>();
            ArrayList<Long> add = new ArrayList<>(labelIds);
            for (Long label : old) {
                if (!add.contains(label)) {
                    delete.add(label);
                } else {
                    add.remove(label);
                }
            }
            if (delete.size() > 0) labelDao.deleteCustomerRelations(id, delete);
            if (add.size() > 0) labelDao.insertCustomerRelations(id, add);
        }
        return null;
    }

    @Override
    public String insertRelation(CustomerRelation relation) {
        int result = customerRelationDao.insert(relation);
        if (result < 1) {
            return "插入失败";
        }
        return null;
    }

    @Override
    public List<CustomerRelation> queryRelationById(Long id) {
        QueryWrapper<CustomerRelation> wrapper = new QueryWrapper<>();
        wrapper.eq("customer_id", id);
        wrapper.orderByAsc("create_time");
        return customerRelationDao.selectList(wrapper);
    }
}
