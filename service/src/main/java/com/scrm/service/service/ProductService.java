package com.scrm.service.service;

import com.scrm.service.entity.Product;

import java.util.List;
import java.util.Map;

/**
 * @author Ganyunhui
 * @create 2021-11-23 15:30
 */
public interface ProductService {
    Integer queryCount();

    List<Map<Object ,Object>> queryProduct(Integer pageNum, Integer pageSize,Integer type,List<Integer> a);

    Integer addProduct(Product product) throws Exception;

    Integer editProduct(Product product) throws Exception;

    Map<Object, Object> productDetail(String id,String shareID);

    List<Map<Object, Object>> queryProductByKey(String keySearch);


}
