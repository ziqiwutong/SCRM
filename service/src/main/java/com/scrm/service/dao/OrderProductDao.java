package com.scrm.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.service.entity.OrderProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.lang.NonNull;

@Mapper
public interface OrderProductDao extends BaseMapper<OrderProduct> {

    @Select("select sum(product_amount) from se_order_product where order_id = #{orderId}")
    Integer queryCountByOrder(@NonNull Long orderId);
}
