package com.scrm.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scrm.service.dao.ProductDao;
import com.scrm.service.entity.Product;
import com.scrm.service.service.ProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Ganyunhui
 * @create 2021-11-23 15:30
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductDao productDao;

    @Override
    public List<Product> queryPage(Integer pageCount, Integer currentPage, QueryWrapper<Product> wrapper) {
        int offset = (currentPage - 1) * pageCount;
        wrapper.last(" limit " + offset + "," + pageCount);
        return productDao.selectList(wrapper);
    }

    @Override
    public int queryCount(QueryWrapper<Product> wrapper) {
        return productDao.selectCount(wrapper);
    }

    @Override
    public Product queryById(Long id) {
        return productDao.selectById(id);
    }

    @Override
    public String insert(Product product) {
        int result = productDao.insert(product);
        if (result < 1) {
            return "插入失败";
        }
        return null;
    }

    @Override
    public String update(Product product) {
        int result = productDao.updateById(product);
        if (result < 1) {
            return "更新失败";
        }
        return null;
    }

    @Override
    public String delete(Long id) {
        int result = productDao.deleteById(id);
        if (result < 1) {
            return "删除失败";
        }
        return null;
    }
}
