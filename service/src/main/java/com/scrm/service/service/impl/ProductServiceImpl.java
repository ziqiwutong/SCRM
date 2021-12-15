package com.scrm.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scrm.service.dao.ProductDao;
import com.scrm.service.entity.Product;
import com.scrm.service.service.ProductService;
import com.scrm.service.vo.ProductArticle;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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
    @Transactional
    public String insert(Product product) {
        int result = productDao.insert(product);
        if (result < 1) {
            return "插入失败";
        }
        ProductArticle article = generateArticle(product);
        result = productDao.generateArticle(article);
        if (result < 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "插入失败";
        }
        Product update = new Product();
        update.setId(product.getId());
        update.setArticleId(article.getId());
        result = productDao.updateById(update);
        if (result < 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "插入失败";
        }
        return null;
    }

    @Override
    @Transactional
    public String update(Product product) {
        if (product.getArticleId() != null) product.setArticleId(null);
        int result = productDao.updateById(product);
        if (result < 1) {
            return "更新失败";
        }
        product = productDao.selectById(product.getId());
        ProductArticle article = generateArticle(product);
        article.setId(product.getArticleId());
        productDao.updateArticle(article);
        return null;
    }

    @Override
    @Transactional
    public String delete(Long id) {
        Product product = productDao.selectById(id);
        if (product == null) {
            return "产品不存在";
        }
        String sourceId = product.getSourceId();
        if (sourceId != null && sourceId.length() > 3 && sourceId.charAt(2) == '_') return "外部产品禁止删除";
        productDao.deleteArticle(product.getArticleId());
        int result = productDao.deleteById(id);
        if (result < 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "删除失败";
        }
        return null;
    }

    /**
     * 更新已有产品的文章
     */
    private void initArticle() {
        List<Product> list = productDao.selectList(null);
        for (Product product : list) {
            ProductArticle article = generateArticle(product);
            if (product.getArticleId() != null) {
                article.setId(product.getArticleId());
                productDao.updateArticle(article);
            } else {
                productDao.generateArticle(article);
                Product update = new Product();
                update.setId(product.getId());
                update.setArticleId(article.getId());
                productDao.updateById(update);
            }
        }
    }

    /**
     * 生成产品文章
     */
    private ProductArticle generateArticle(Product product) {
        ProductArticle article = new ProductArticle();
        article.setProductIdList("[" + product.getId() + "]");
        article.setProductName(product.getProductName());
        article.setProductImage(product.getProductImage());
        String context = "<div style=\"padding-top: 45px; padding-bottom: 60px;\">";
        if (product.getProductImage() != null) {
            context = context + "<img src=" + product.getProductImage() +
                    " width=\"100%\" style=\"display: table-cell; text-align:center; margin: 0 auto\"/>";
        }
        context = context + "<div>" +
                "<div style=\"color: #F74A4A; font-weight: bold; margin: 10px 0 3px 15px;\">￥" +
                (product.getProductPrice() == null ? "" : product.getProductPrice()) +
                "</div>" +
                "<div style=\"margin-left:15px;\">" +
                (product.getProductName() == null ? "" : product.getProductName()) +
                "</div>" +
                "<div style=\"width: 90%; margin: 10px auto 0 auto; text-align: justify; border-radius: 10px;\">" +
                "<div style=\"margin: 0 5px; font-size: 1rem; line-height: 25px; text-indent:35px; letter-spacing:1px;\">" +
                (product.getProductIntro() == null ? "" : product.getProductIntro()) +
                "</div>" +
                "</div>" +
                "</div>" +
                "</div>";
        article.setArticleContext(context);
        return article;
    }
}
