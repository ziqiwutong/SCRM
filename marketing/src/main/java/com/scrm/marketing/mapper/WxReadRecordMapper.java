package com.scrm.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.marketing.entity.WxReadRecord;
import com.scrm.marketing.entity.wrapper.SharePersonWrapper;
import com.scrm.marketing.mapper.sqlbuilder.WxReadRecordSqlProvider;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.time.LocalDate;
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
    @Select("SELECT DISTINCT share_id FROM mk_wx_read_record WHERE article_id=#{articleId}")
    List<String> queryShareIds(long articleId);

    @SuppressWarnings("all")
    @Select("SELECT article_id,SUM(read_time) read_time,read_date " +
            " FROM mk_wx_read_record" +
            " WHERE article_id=#{articleId} " +
            " AND #{startDate}<read_date AND read_date<=CURRENT_DATE" +
            " GROUP BY article_id,read_date")
    List<WxReadRecord> queryArticleRead(Long articleId, LocalDate startDate);

    @SuppressWarnings("all")
    @Select("SELECT article_id,share_id,SUM(read_time) readTimeSum,COUNT(*) read_times" +
            " FROM mk_wx_read_record" +
            " WHERE article_id=#{articleId}" +
            " GROUP BY article_id,share_id" +
            " ORDER BY readTimeSum DESC")
    List<SharePersonWrapper> queryArtSharePerson(long articleId);

    @SuppressWarnings("all")
    @Select("SELECT r.wid,SUM(r.read_time) read_time,r.openid," +
            " (SELECT nickname FROM mk_wx_user WHERE id=r.wid) nickname," +
            " (SELECT headimgurl FROM mk_wx_user WHERE id=r.wid) headimgurl" +
            " FROM mk_wx_read_record r" +
            " GROUP BY r.wid,r.openid" +
            " ORDER BY read_time DESC" +
            " LIMIT #{pageSize} OFFSET #{offset}")
    List<WxReadRecord> queryAllWxRead(int offset, int pageSize);

    @SuppressWarnings("all")
    @Select("SELECT wid,openid,SUM(read_time) read_time,read_date" +
            " FROM mk_wx_read_record" +
            " WHERE wid=#{wid} AND " +
            " openid=(SELECT openid FROM mk_wx_user WHERE id=#{wid})" +
            " AND #{startDate}<read_date AND read_date<=CURRENT_DATE" +
            " GROUP BY wid,openid,read_date")
    List<WxReadRecord> queryOneWxRead(Long wid, LocalDate startDate);
}
