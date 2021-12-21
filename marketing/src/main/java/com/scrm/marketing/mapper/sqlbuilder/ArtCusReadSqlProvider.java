package com.scrm.marketing.mapper.sqlbuilder;

import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import java.time.LocalDate;

/**
 * @author fzk
 * @date 2021-10-22 17:07
 */
public class ArtCusReadSqlProvider implements ProviderMethodResolver {
    public static String queryCusRead(Long customerId, LocalDate startDate, Integer offset, Integer pageSize) {
        String sql;
        // 情况1：无customerId，分页查询客户阅读总时长
        if (customerId == null)
            /*
             * 这里不要内连接直接将客户头像和昵称查出来，不然不会走索引(customer_id,read_date)，从而全文扫描
             * 虽有两次子查询，但走聚族索引并不慢
             */
            sql = new SQL() {{
                SELECT("r.customer_id,SUM(r.read_time) read_time");
                SELECT("(SELECT c.customer_icon FROM se_customer c WHERE c.id=r.customer_id) customer_icon");
                SELECT("(SELECT c.customer_name FROM se_customer c WHERE c.id=r.customer_id) customer_name");
                FROM("mk_article_customer_read r");
                GROUP_BY("r.customer_id");
                ORDER_BY("read_time DESC");
                LIMIT(pageSize);
                OFFSET(offset);
            }}.toString();

            // 情况2：有customerId，sevenFlag为true，查1周内客户每天阅读时长
            // 情况3：有customerId，sevenFlag为false，查1个月内客户每天阅读时长
        else // 走索引(customer_id,read_date)
            sql = new SQL() {{
                SELECT("customer_id,read_date,SUM(read_time) read_time");
                FROM("mk_article_customer_read");
                WHERE("customer_id=" + customerId);
                WHERE("#{startDate}<=read_date AND read_date < CURRENT_DATE");
                GROUP_BY("customer_id,read_date");
            }}.toString();

        return sql;
    }
}
