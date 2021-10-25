package com.scrm.service.service;

import com.scrm.service.entity.Customer;

import java.util.List;

public interface CustomerService {

    /**
     * 查询Customer
     * @param pageCount 每页数量
     * @param currentPage 当前页数
     * @return Customer
     */
    List<Customer> query(Integer pageCount, Integer currentPage);

    /**
     * 查询Customer总数量
     * @return Customer数量
     */
    Integer queryCount();

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
}
