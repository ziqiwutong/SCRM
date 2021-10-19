package com.scrm.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.marketing.entity.ArticleShareRecord;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author fzk
 * @date 2021-10-14 17:56
 */
public interface ArticleShareRecordMapper extends BaseMapper<ArticleShareRecord> {
    @Delete("DELETE FROM mk_article_share_record WHERE article_id=#{articleId}")
    int deleteByArticleId(Long articleId);

    @Select("SELECT * FROM mk_article_share_record WHERE article_id=#{id} AND share_id=#{shareId}")
    List<ArticleShareRecord> selectByAIdAndSid(Long id, Long shareId);
}
