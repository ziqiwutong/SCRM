package com.scrm.service.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scrm.service.entity.Customer;
import com.scrm.service.entity.CustomerCustomizedField;
import com.scrm.service.entity.CustomerRelation;

import java.util.HashSet;
import java.util.List;

public interface CustomerService {

    /**
     * 查询Customer
     * @return List<Customer>
     */
    List<Customer> query(QueryWrapper<Customer> wrapper);

    /**
     * 分页查询Customer
     * @param pageCount 每页数量
     * @param currentPage 当前页数
     * @return List<Customer>
     */
    List<Customer> queryPage(Integer pageCount, Integer currentPage, QueryWrapper<Customer> wrapper);

    /**
     * 根据商机创建时间查询Customer ID
     * @param start 开始日期（闭区间）
     * @param end 结束日期（开区间）
     * @return List<Long>
     */
    List<Long> queryIdByBusinessTime(String start, String end);

    /**
     * 查询Customer总数量
     * @return Customer数量
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
     * 查询CustomerCustomizedField
     * @return List<CustomerCustomizedField>
     */
    List<CustomerCustomizedField> queryCustomizedField();

    /**
     * 给用户打标签
     * @param customerIds Customer ID Set
     * @param labelIds Label ID List
     */
    String label(HashSet<Long> customerIds, List<Long> labelIds);

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

    /**
     * 绑定微信用户openid和微信昵称到客户表
     * 同时会将客户状态更新到微信用户表
     * @param customerId 客户id
     * @param wx_name 微信昵称
     * @param wx_openid openid
     * @return null表示绑定成功
     */
    String bindWxUser(long customerId, String wx_name, String wx_openid);
}
