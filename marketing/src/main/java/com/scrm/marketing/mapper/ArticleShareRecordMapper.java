package com.scrm.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.marketing.entity.ArticleShareRecord;
import com.scrm.marketing.mapper.sqlbuilder.ArticleShareRecordSqlProvider;
import org.apache.ibatis.annotations.*;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @author fzk
 * @date 2021-10-14 17:56
 */
@Deprecated(since = "2021-11-28",forRemoval = true)
public interface ArticleShareRecordMapper extends BaseMapper<ArticleShareRecord> {
    @SuppressWarnings("all")
    @Delete("DELETE FROM mk_article_share_record WHERE article_id=#{articleId}")
    int deleteByArticleId(Long articleId);

    @SuppressWarnings("all")
    @Select("SELECT * FROM mk_article_share_record WHERE article_id=#{articleId} AND share_id=#{shareId}")
    List<ArticleShareRecord> selectByAIdAndSid(Long articleId, Long shareId);

    @SelectProvider(ArticleShareRecordSqlProvider.class)
        // 默认实现中，会将映射器方法的调用解析到实现的同名方法上
    List<ArticleShareRecord> selectByAidAndSids(Long articleId, List<Long> shareIds);

    @UpdateProvider(ArticleShareRecordSqlProvider.class)
    int addReadRecord(Long id, String newReadRecord_json, boolean newOpenidFlag, String newOpenids_json);
}
