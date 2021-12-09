package com.scrm.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.service.dao.sqlprovider.CustomerSqlProvider;
import com.scrm.service.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CustomerDao extends BaseMapper<Customer> {

    @SelectProvider(CustomerSqlProvider.class)
    List<Long> queryIdByBusinessTime(String start, String end);

    @SuppressWarnings("all")
    @Update("UPDATE se_customer SET wx_name=#{wx_name},wx_openid=#{wx_openid} WHERE id=#{customer_id}")
    int bindWxUser(long customerId, String wx_name, String wx_openid);

    @SuppressWarnings("all")
    @Update("UPDATE mk_wx_user SET reader_status=(SELECT customer_status FROM se_customer WHERE id=#{customerId})" +
            " WHERE openid=#{openid}")
    int copyCusStatusToWxUser(long customerId, String openid);
}
