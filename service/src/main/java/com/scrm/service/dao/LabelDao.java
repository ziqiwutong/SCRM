package com.scrm.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.service.entity.Label;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.lang.NonNull;

import java.util.List;

@Mapper
public interface LabelDao extends BaseMapper<Label> {

    @Select("select * from se_label where id in ("
            + "select label_id from se_customer_label_relation where customer_id = #{customerId})")
    List<Label> queryByCustomerId(@NonNull Long customerId);
}
