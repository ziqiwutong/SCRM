package com.scrm.marketing.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
/**
 * @author fzk
 * @date 2021-11-24 17:52
 */
@Mapper
public interface CustomerMapper {
    @SuppressWarnings("all")
    @Select("SELECT id FROM se_customer WHERE wx_openid=#{wx_openid} LIMIT 1")
    Long queryIdByOpenid(String wx_openid);

    @SuppressWarnings("all")
    @Select("SELECT customer_status FROM se_customer WHERE id=#{cusId}")
    String queryCusStatusById(Long cusId);
}
