package com.scrm.service.dao;

import com.scrm.service.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Map;

/**
 * @author Ganyunhui
 * @create 2021-10-20 19:18
 */
@Mapper
public interface ProductDao {
    List<Map<Object ,Object> > queryProduct(Integer pageNum, Integer pageSize,Integer type,List<Integer> a);

    Integer queryCount();

    Integer addProduct(Product product);

    Integer editProduct(Product product);

    Map<Object, Object> productDetail(String id,String shareID);

    List<Map<Object, Object>> queryProductByKey(String keySearch);

    @Select("select * from se_product where source_id = #{sourceId}")
    List<Product> queryBySourceId(@NonNull String sourceId);

    @Select("select * from se_product where source_id like '02_%' limit #{start},#{size}")
    List<Product> queryWeimobProduct(Integer start, Integer size);

    @Select("select count(id) from se_product where source_id like '02_%'")
    Integer queryWeimobCount();
}
