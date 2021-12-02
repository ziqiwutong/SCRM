package com.scrm.service.dao;

import com.scrm.service.entity.Product;
import org.apache.ibatis.annotations.Mapper;

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

}
