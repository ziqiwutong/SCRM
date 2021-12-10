package com.scrm.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.marketing.entity.WxReadRecord;
import com.scrm.marketing.mapper.sqlbuilder.WxReadRecordSqlProvider;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @author fzk
 * @date 2021-11-28 14:39
 */
public interface WxReadRecordMapper extends BaseMapper<WxReadRecord> {
    @SuppressWarnings("all")
    @Delete("DELETE FROM mk_wx_read_record WHERE article_id=#{article_id}")
    int deleteByAid(Long articleId);

    @SelectProvider(WxReadRecordSqlProvider.class)
    List<WxReadRecord> queryByAidAndSids(Long articleId, List<String> shareIds, int offset, int pageSize);

    @SelectProvider(WxReadRecordSqlProvider.class)
    int queryReadTimes(Long articleId, List<String> shareIds);

    @SelectProvider(WxReadRecordSqlProvider.class)
    int queryReadPeople(Long articleId, List<String> shareIds);

    @SuppressWarnings("all")
    @Select("SELECT DISTINCT share_id FROM mk_wx_read_record WHERE id=#{articleId}")
    List<String> queryShareIds(long articleId);
}
