package com.scrm.marketing.mapper.sqlbuilder;

import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @author fzk
 * @date 2021-10-20 0:20
 */
@Deprecated(since = "2021-11-28",forRemoval = true)
public class ArticleShareRecordSqlProvider implements ProviderMethodResolver {
    public static String selectByAidAndSids(final Long articleId, final List<Long> shareIds) {
        return new SQL() {{
            SELECT("*");
            FROM("mk_article_share_record");
            WHERE("article_id=" + articleId.toString());
            if (shareIds != null) {
                if (shareIds.size() == 1) {
                    WHERE("share_id=" + shareIds.get(0));
                }
                if (shareIds.size() > 1) {
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

    public static String addReadRecord(final Long id, final String newReadRecord_json, final boolean newOpenidFlag, final String newOpenids_json) {
        return new SQL() {{
            UPDATE("mk_article_share_record");
            SET("read_record=#{newReadRecord_json}");
            SET("read_times=read_times+1");
            if (newOpenidFlag) {
                SET("openids=#{newOpenids_json}");
                SET("read_people=read_people+1");
            }
            WHERE("id=" + id);
        }}.toString();
    }
}
