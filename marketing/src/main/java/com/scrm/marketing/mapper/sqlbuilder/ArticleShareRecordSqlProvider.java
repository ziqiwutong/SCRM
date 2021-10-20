package com.scrm.marketing.mapper.sqlbuilder;

import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @author fzk
 * @date 2021-10-20 0:20
 */
public class ArticleShareRecordSqlProvider implements ProviderMethodResolver {
    public String selectByAidAndSids(final Long articleId, @Nullable final List<Long> shareIds) {
        return new SQL() {{
            SELECT("*");
            FROM("mk_article_share_record");
            WHERE("article_id="+articleId.toString());
            if (shareIds != null && shareIds.size() != 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("(");
                for (Long shareId : shareIds) {
                    sb.append(shareId);
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append(")");
                WHERE("share_id in "+sb.toString());
            }
        }}.toString();
    }
}
