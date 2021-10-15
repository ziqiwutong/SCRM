package com.scrm.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.marketing.entity.ArticleCustomerRead;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Repository;

/**
 * @author fzk
 * @date 2021-10-14 17:55
 */
@Repository
public interface ArticleCustomerReadMapper extends BaseMapper<ArticleCustomerRead> {
    @Delete("DELETE FROM mk_article_customer_read WHERE article_id=#{articleId}")
    int deleteByArticleId(Long articleId);
}
