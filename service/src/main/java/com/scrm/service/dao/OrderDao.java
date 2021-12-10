package com.scrm.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.service.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author Ganyunhui
 * @create 2021-11-03 20:20
 */
@Mapper
public interface OrderDao extends BaseMapper<Order> {

    @Select("select * from se_order where order_num = #{orderNum}")
    List<Order> queryByOrderNum(@NonNull String orderNum);
}
