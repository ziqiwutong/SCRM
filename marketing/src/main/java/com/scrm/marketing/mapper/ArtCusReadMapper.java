package com.scrm.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.marketing.entity.ArticleCustomerRead;
import com.scrm.marketing.mapper.sqlbuilder.ArtCusReadSqlProvider;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author fzk
 * @date 2021-10-14 17:55
 */
@Repository
public interface ArtCusReadMapper extends BaseMapper<ArticleCustomerRead> {
    @Delete("DELETE FROM mk_article_customer_read WHERE article_id=#{articleId}")
    int deleteByArticleId(Long articleId);

    @Select("SELECT article_id,SUM(read_time) read_time,read_date " +
            " FROM mk_article_customer_read" +
            " WHERE article_id=#{articleId} " +
            " AND #{startDate}<read_date AND read_date<=CURRENT_DATE" +
            " GROUP BY article_id,read_date")
    List<ArticleCustomerRead> queryArticleRead(Long articleId, Date startDate);

    @SelectProvider(ArtCusReadSqlProvider.class)
    List<ArticleCustomerRead> queryCusRead(Long customerId, Date startDate, Integer offset, Integer pageSize);


    /**
     * 查询mk_article_customer_read表中不同的客户数量
     *
     * @return 客户数
     */
    @Select("SELECT COUNT(DISTINCT customer_id) FROM mk_article_customer_read;")
    int queryCusCount();
}
