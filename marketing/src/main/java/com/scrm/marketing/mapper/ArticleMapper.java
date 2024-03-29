package com.scrm.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.marketing.entity.Article;
import com.scrm.marketing.mapper.sqlbuilder.ArticleSqlProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author fzk
 * @date 2021-10-14 17:03
 */
public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 除了文章内容外，其余字段均查询出来了
     *
     * @param offset   偏移量
     * @param pageSize 页大小
     * @return 文章列表
     */
    @SuppressWarnings("all")
    @SelectProvider(ArticleSqlProvider.class)
    // 默认实现中，会将映射器方法的调用解析到实现的同名方法上
    List<Article> queryPage(int offset, int pageSize, Integer examineFlag, Integer materialType);

    @SuppressWarnings("all")
    @SelectProvider(ArticleSqlProvider.class)
        // 默认实现中，会将映射器方法的调用解析到实现的同名方法上
    int queryCount(int offset, int pageSize, Integer examineFlag, Integer materialType);

    @SuppressWarnings("all")
    @Update("UPDATE mk_article SET examine_id=#{loginId},examine_name=#{examineName}, " +
            " examine_flag=#{examineFlag},examine_notes=#{examineNotes}" +
            " WHERE id=#{id} ")
    int examine(Long id, String loginId, String examineName, Integer examineFlag, String examineNotes);

    @SuppressWarnings("all")
    @Select("SELECT id,author_id,author_name,article_title,article_image," +
            "product_ids_json,article_view_times,article_read_time_sum," +
            "examine_flag,examine_id,examine_name,examine_notes," +
            "article_origin_author,article_account_name,article_power,article_type," +
            "material_type," +
            "create_time,update_time" +
            "   FROM mk_article" +
            "   WHERE examine_flag=#{examineFlag}" +
            "   AND article_title LIKE '%' #{title} '%' ")
    List<Article> queryByTitle(String title, int examineFlag);

    @SuppressWarnings("all")
    @Update("UPDATE mk_article SET article_view_times=article_view_times+1,article_read_time_sum=article_read_time_sum + #{readTime} WHERE id=#{articleId}")
    int addArticleRead(Long articleId, long readTime);

}