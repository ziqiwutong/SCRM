package com.scrm.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.service.entity.Customer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerDao extends BaseMapper<Customer> {
}
