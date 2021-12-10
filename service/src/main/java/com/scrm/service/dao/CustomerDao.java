package com.scrm.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.service.dao.sqlprovider.CustomerSqlProvider;
import com.scrm.service.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CustomerDao extends BaseMapper<Customer> {

    @SelectProvider(CustomerSqlProvider.class)
    List<Long> queryIdByBusinessTime(String start, String end);
}
