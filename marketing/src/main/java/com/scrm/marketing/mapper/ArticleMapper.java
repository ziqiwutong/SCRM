package com.scrm.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.marketing.entity.Article;
import com.scrm.marketing.mapper.sqlbuilder.ArticleSqlProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author fzk
 * @date 2021-10-14 17:03
 */
@Repository
public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 除了文章内容外，其余字段均查询出来了
     *
     * @param offset   偏移量
     * @param pageSize 页大小
     * @return 文章列表
     */
    @SelectProvider(ArticleSqlProvider.class)
    // 默认实现中，会将映射器方法的调用解析到实现的同名方法上
    List<Article> queryPage(int offset, int pageSize, Integer examineFlag);

    @SelectProvider(ArticleSqlProvider.class)
        // 默认实现中，会将映射器方法的调用解析到实现的同名方法上
    int queryCount(int offset, int pageSize, Integer examineFlag);

    @Update("UPDATE mk_article SET examine_id=#{loginId},examine_name=#{examineName}, " +
            " examine_flag=#{examineFlag},examine_notes=#{examineNotes} " +
            " WHERE id=#{id} ")
    int examine(Long id, Long loginId, String examineName, Integer examineFlag, String examineNotes);

    @Select("SELECT id,author_id,author_name,article_title,article_image," +
            "product_id,article_view_times,article_read_time_sum," +
            "examine_flag,examine_id,examine_name,examine_notes," +
            "article_origin_author,article_account_name,article_power,article_type," +
            "create_time,update_time" +
            "   FROM mk_article" +
            "   WHERE examine_flag=#{examineFlag}" +
            "   AND article_title LIKE '%' #{title} '%' ")
    List<Article> queryByTitle(String title, int examineFlag);

}