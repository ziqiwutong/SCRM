package com.scrm.service.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scrm.service.entity.Customer;

import java.util.List;

public interface CustomerService {

    /**
     * 查询Customer
     * @param pageCount 每页数量
     * @param currentPage 当前页数
     * @return Customer
     */
    List<Customer> query(Integer pageCount, Integer currentPage, QueryWrapper<Customer> wrapper);

    /**
     * 查询Customer总数量
     * @return Customer数量
     * @param wrapper
     */
    Integer queryCount(QueryWrapper<Customer> wrapper);

    /**
     * 查询Customer
     * @param id Customer ID
     * @return Customer
     */
    Customer queryById(Integer id);

    /**
     * 插入Customer
     * @param customer Customer
     * @return null表示插入成功
     */
    String insert(Customer customer);

    /**
     * 批量插入Customer
     * @param customers Customer List
     * @return null表示插入成功
     */
    String insertBatch(List<Customer> customers);

    /**
     * 更新Customer
     * @param customer Customer
     * @return null表示更新成功
     */
    String update(Customer customer);

    /**
     * 删除Customer
     * @param id Customer ID
     */
    String delete(Integer id);

    /**
     * 批量删除Customer
     * @param idList Customer ID List
     */
    String deleteBatch(List<Integer> idList);
}
