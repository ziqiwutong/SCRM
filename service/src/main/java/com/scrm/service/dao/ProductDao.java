package com.scrm.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.service.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author Ganyunhui
 * @create 2021-10-20 19:18
 */
@Mapper
public interface ProductDao extends BaseMapper<Product> {

    @Select("select * from se_product where source_id = #{sourceId}")
    List<Product> queryBySourceId(@NonNull String sourceId);

    @Select("select * from se_product where source_id like '02_%' limit #{start},#{size}")
    List<Product> queryWeimobProduct(Integer start, Integer size);

    @Select("select count(id) from se_product where source_id like '02_%'")
    Integer queryWeimobCount();
}
