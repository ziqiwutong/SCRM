package com.scrm.marketing.mapper.sqlbuilder;

import com.scrm.marketing.entity.Article;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * @author fzk
 * @date 2021-10-15 21:45
 */
public class ArticleSqlProvider implements ProviderMethodResolver {
    public static String queryPage(final int offset, final int pageSize, final Integer examineFlag) {

        return new SQL() {{
            SELECT("id,author_id,author_name,article_title,article_image," +
                    "product_id,article_view_times,article_read_time_sum," +
                    "examine_flag,examine_id,examine_name,examine_notes," +
                    "article_origin_author,article_account_name,article_power,article_type," +
                    "create_time,update_time");
            FROM("mk_article");
            if (examineFlag != null) {
                WHERE("examine_flag=" + examineFlag);
            }
            LIMIT(pageSize);
            OFFSET(offset);
        }}.toString();
    }

    public static String queryCount(final int offset, final int pageSize, final Integer examineFlag) {
        return new SQL() {{
            SELECT("COUNT(*)");
            FROM("mk_article");
            if (examineFlag != null) {
                WHERE("examine_flag=" + examineFlag);
            }
        }}.toString();
    }
}
