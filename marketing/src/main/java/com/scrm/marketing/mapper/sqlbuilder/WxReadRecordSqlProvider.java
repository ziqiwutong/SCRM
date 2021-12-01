package com.scrm.marketing.mapper.sqlbuilder;

import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * @author fzk
 * @date 2021-11-28 17:03
 */
public class WxReadRecordSqlProvider implements ProviderMethodResolver {
    public static String queryByAidAndSids(Long articleId, List<Long> shareIds, int offset, int pageSize) {
        return new SQL() {{
            SELECT("*");
            FROM("mk_wx_read_record");
            WHERE("article_id=" + articleId);
            if (shareIds != null) {
                if (shareIds.size() == 1)
                    WHERE("share_id=" + shareIds.get(0));
                else if (shareIds.size() > 1) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("(");
                    for (Long shareId : shareIds) {
                        sb.append(shareId);
                        sb.append(",");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    sb.append(")");
                    WHERE("share_id in " + sb.toString());
                }
            }
            ORDER_BY("read_date DESC,create_time DESC");
            LIMIT(pageSize);
            OFFSET(offset);
        }}.toString();
    }

    public static String queryReadTimes(Long articleId, List<Long> shareIds) {
        return new SQL() {{
            SELECT("COUNT(*)");
            FROM("mk_wx_read_record");
            WHERE("article_id=" + articleId);
            if (shareIds != null) {
                if (shareIds.size() == 1)
                    WHERE("share_id=" + shareIds.get(0));
                else if (shareIds.size() > 1) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("(");
                    for (Long shareId : shareIds) {
                        sb.append(shareId);
                        sb.append(",");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    sb.append(")");
                    WHERE("share_id in " + sb.toString());
                }
            }
        }}.toString();
    }

    public static String queryReadPeople(Long articleId, List<Long> shareIds) {
        return new SQL() {{
            SELECT("COUNT(DISTINCT wid)");
            FROM("mk_wx_read_record");
            WHERE("article_id=" + articleId);
            if (shareIds != null) {
                if (shareIds.size() == 1)
                    WHERE("share_id=" + shareIds.get(0));
                else if (shareIds.size() > 1) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("(");
                    for (Long shareId : shareIds) {
                        sb.append(shareId);
                        sb.append(",");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    sb.append(")");
                    WHERE("share_id in " + sb.toString());
                }
            }
        }}.toString();
    }
}
