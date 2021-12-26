package com.scrm.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.marketing.entity.Product;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author fzk
 * @date 2021-10-20 21:34
 */
public interface ProductMapper extends BaseMapper<Product> {
    @SuppressWarnings("all")
    @Select("select * from se_product limit #{pageSize} offset #{offset}")
    List<Product> queryProPurchase(int offset, Integer pageSize);

    @SuppressWarnings("all")
    @Select("SELECT * FROM se_product WHERE type_id=#{typeId}")
    List<Product> queryByTypeId(Long typeId);

    @SuppressWarnings("all")
    @Update("UPDATE se_product SET product_view_times=product_view_times+1 WHERE article_id=#{articleId}")
    int addProductViewTimes(long articleId);
}
