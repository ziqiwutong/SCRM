package com.scrm.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.marketing.entity.ArticleShareRecord;
import com.scrm.marketing.mapper.sqlbuilder.ArticleShareRecordSqlProvider;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @author fzk
 * @date 2021-10-14 17:56
 */
public interface ArticleShareRecordMapper extends BaseMapper<ArticleShareRecord> {
    @Delete("DELETE FROM mk_article_share_record WHERE article_id=#{articleId}")
    int deleteByArticleId(Long articleId);

    @Select("SELECT * FROM mk_article_share_record WHERE article_id=#{articleId} AND share_id=#{shareId}")
    List<ArticleShareRecord> selectByAIdAndSid(Long articleId, Long shareId);

    @SelectProvider(ArticleShareRecordSqlProvider.class)
        // 默认实现中，会将映射器方法的调用解析到实现的同名方法上
    List<ArticleShareRecord> selectByAidAndSids(Long articleId, @Nullable List<Long> shareIds);

    @Update("UPDATE mk_article_share_record SET read_record=#{readRecord}" +
            "   WHERE article_id=#{articleId} AND share_id=${shareId}")
    int updateReadRecord(Long articleId, Long shareId, String readRecord);
}
