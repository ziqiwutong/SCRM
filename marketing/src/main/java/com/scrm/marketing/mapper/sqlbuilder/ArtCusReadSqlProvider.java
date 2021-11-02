package com.scrm.marketing.mapper.sqlbuilder;

import com.scrm.marketing.entity.ArticleCustomerRead;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;

/**
 * @author fzk
 * @date 2021-10-22 17:07
 */
public class ArtCusReadSqlProvider implements ProviderMethodResolver {
    public String queryCusRead(Long customerId, Date startDate, Integer offset, Integer pageSize) {
        String sql;
        // 情况1：无customerId，分页查询客户阅读总时长
        if (customerId == null)
            sql = new SQL() {{
                SELECT("customer_id,SUM(read_time) read_time");
                FROM("mk_article_customer_read");
                GROUP_BY("customer_id");
                LIMIT(pageSize);
                OFFSET(offset);
            }}.toString();

            // 情况2：有customerId，sevenFlag为true，查1周内客户每天阅读时长
            // 情况3：有customerId，sevenFlag为false，查1个月内客户每天阅读时长
        else
            sql = new SQL() {{
                SELECT("customer_id,read_date,SUM(read_time) read_time");
                FROM("mk_article_customer_read");
                WHERE("customer_id=" + customerId);
                WHERE("#{startDate}<read_date AND read_date < CURRENT_DATE");
                GROUP_BY("customer_id,read_date");
            }}.toString();

        return sql;
    }
}
