package com.scrm.service.service.impl;

import com.scrm.service.dao.ProductDao;
import com.scrm.service.entity.OrderAndOrderDetail;
import com.scrm.service.entity.Product;
import com.scrm.service.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Ganyunhui
 * @create 2021-11-23 15:30
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductDao productDao;

    @Override
    public Integer queryCount() {
        return productDao.queryCount();
    }

    @Override
    public List<Map<Object, Object>> queryProduct(Integer pageNum, Integer pageSize,Integer type,List<Integer> a) {
        return productDao.queryProduct(pageNum, pageSize,type,a);
    }

    @Override
    @Transactional
    public Integer addProduct(Product product) throws Exception {
        int result = productDao.addProduct(product);
        if (result < 1) {
            throw new Exception("插入失败");
        }
        return 0;
    }

    @Override
    @Transactional//开启事务
    public Integer editProduct(Product product) throws Exception {
        int result = productDao.editProduct(product);
        if (result < 1) {
            throw new Exception("更新失败");
        }
        return null;
    }

    @Override
    public Map<Object, Object> productDetail(String id,String shareID) {
        return productDao.productDetail(id,shareID);
    }

    @Override
    public List<Map<Object, Object>> queryProductByKey(String keySearch) {
        return productDao.queryProductByKey(keySearch);
    }
}
