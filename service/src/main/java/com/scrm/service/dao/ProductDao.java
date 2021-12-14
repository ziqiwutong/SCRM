package com.scrm.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.service.entity.Product;
import com.scrm.service.vo.ProductArticle;
import org.apache.ibatis.annotations.*;
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

    @Insert("insert into mk_article (" +
            "author_id, author_name, product_ids_json, article_title, article_context, article_image" +
            ") value (" +
            "'-1', 'SCRM', #{productIdList}, #{productName}, #{articleContext}, #{productImage}" +
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer generateArticle(ProductArticle productArticle);

    @Update("update mk_article set " +
            "article_title = #{productName}, article_context = #{articleContext}, article_image = #{productImage} " +
            "where id = #{id}")
    void updateArticle(ProductArticle productArticle);

    @Delete("delete from mk_article where id = #{id}")
    void deleteArticle(Long id);
}
