package com.scrm.service.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scrm.service.entity.Product;

import java.util.List;

/**
 * @author Ganyunhui
 * @create 2021-11-23 15:30
 */
public interface ProductService {

    List<Product> queryPage(Integer pageCount, Integer currentPage, QueryWrapper<Product> wrapper);

    int queryCount(QueryWrapper<Product> wrapper);

    Product queryById(Long id);

    String insert(Product product);

    String update(Product product);

    String delete(Long id);
}
