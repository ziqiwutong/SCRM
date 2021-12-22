package com.scrm.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.marketing.entity.ArticleCustomerRead;
import com.scrm.marketing.mapper.sqlbuilder.ArtCusReadSqlProvider;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.util.List;

/**
 * @author fzk
 * @date 2021-10-14 17:55
 */
public interface ArtCusReadMapper extends BaseMapper<ArticleCustomerRead> {
    @SuppressWarnings("all")
    @Delete("DELETE FROM mk_article_customer_read WHERE article_id=#{articleId}")
    int deleteByArticleId(Long articleId);

    @SuppressWarnings("all")
    @Select("SELECT article_id,SUM(read_time) read_time,read_date " +
            " FROM mk_article_customer_read" +
            " WHERE article_id=#{articleId} " +
            " AND #{startDate}<=read_date AND read_date<=CURRENT_DATE" +
            " GROUP BY article_id,read_date")
    List<ArticleCustomerRead> queryArticleRead(Long articleId, LocalDate startDate);

    @SelectProvider(ArtCusReadSqlProvider.class)
    @SuppressWarnings("all")
    List<ArticleCustomerRead> queryCusRead(Long customerId, LocalDate startDate, Integer offset, Integer pageSize);



    /**
     * 查询mk_article_customer_read表中不同的客户数量
     *
     * @return 客户数
     */
    @SuppressWarnings("all")
    @Select("SELECT COUNT(DISTINCT customer_id) FROM mk_article_customer_read;")
    int queryCusCount();

    @SuppressWarnings("all")
    @Select("SELECT id FROM mk_article_customer_read WHERE article_id=#{articleId} " +
            "   AND customer_id=#{customerId}" +
            "   AND read_date=#{readDate}")
    List<Long> queryTodayRead(long articleId, long customerId, String readDate);

    @SuppressWarnings("all")
    @Update("UPDATE mk_article_customer_read SET read_time=read_time+#{readTime} WHERE id=#{artCusReadId}")
    void addReadTime(long artCusReadId, Integer readTime);
}
