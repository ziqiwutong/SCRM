package com.scrm.service.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scrm.service.entity.Customer;
import com.scrm.service.entity.CustomerRelation;

import java.util.List;

public interface CustomerService {

    /**
     * 查询Customer
     * @param pageCount 每页数量
     * @param currentPage 当前页数
     * @return List<Customer>
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
    Customer queryById(Long id);

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
    String delete(Long id);

    /**
     * 批量删除Customer
     * @param idList Customer ID List
     */
    String deleteBatch(List<Long> idList);

    /**
     * 给用户打标签
     * @param id
     * @param labelIds Label ID List
     */
    String label(Long id, List<Long> labelIds);

    /**
     * 插入CustomerRelation
     * @param relation CustomerRelation
     * @return null表示插入成功
     */
    String insertRelation(CustomerRelation relation);

    /**
     * 查询CustomerRelation
     * @param id Customer ID
     * @return List<CustomerRelation>
     */
    List<CustomerRelation> queryRelationById(Long id);
}
