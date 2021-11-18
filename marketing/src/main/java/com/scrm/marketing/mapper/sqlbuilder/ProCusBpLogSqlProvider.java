package com.scrm.marketing.mapper.sqlbuilder;

import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

/**
 * @author fzk
 * @date 2021-10-21 0:03
 * @see com.scrm.marketing.mapper.ProCusBpLogMapper#queryCusPurchase(Long, Long, Integer, Integer)
 */
public class ProCusBpLogSqlProvider implements ProviderMethodResolver {
    public static String queryCusPurchase(Long customerId, Long productTypeId, Integer offset, Integer pageSize) {
        int condition;
        // 情况1：分页查询所有客户购买情况
        if (customerId == null) condition = 1;
            // 情况2：查询 特定客户 购买的 所有种类 购买情况
        else if (productTypeId == null) condition = 2;
            // 情况3：查询 特定客户 购买的 特定种类 购买情况
        else condition = 3;
        String sql;
        if (condition == 1)
            sql = new SQL() {{
                SELECT("customer_id,customer_name,customer_icon,SUM(purchase_num) purchase_num");
                FROM("mk_product_customer_bp_log");
                GROUP_BY("customer_id,customer_name,customer_icon");
                ORDER_BY("purchase_num DESC");
                LIMIT(pageSize);
                OFFSET(offset);
            }}.toString();

        else if (condition == 2)
            sql = new SQL() {{
                SELECT("customer_id,product_type_id,product_type_name,SUM(purchase_num) purchase_num");
                FROM("mk_product_customer_bp_log");
                WHERE("customer_id=" + customerId);
                GROUP_BY("customer_id,product_type_id,product_type_name");
            }}.toString();
        else
            sql = new SQL() {{
                SELECT("product_id,product_name,product_type_id,product_type_name,purchase_num");
                FROM("mk_product_customer_bp_log");
                WHERE("customer_id=" + customerId);
                WHERE("product_type_id=" + productTypeId);
            }}.toString();
        return sql;
    }


    public static String queryProBrowse(Long productTypeId) {
        String sql;
        // 情况1：查询所有产品类型的总浏览时长
        if (productTypeId == null)
            sql = new SQL() {{
                SELECT("product_type_id,product_type_name,SUM(product_browse_time) product_browse_time");
                FROM("mk_product_customer_bp_log");
                GROUP_BY("product_type_id,product_type_name");
            }}.toString();
            // 情况2：查询某类产品下所有产品的浏览时长
        else
            sql = new SQL() {{
                SELECT("product_id,product_name,SUM(product_browse_time) product_browse_time");
                FROM("mk_product_customer_bp_log");
                WHERE("product_type_id=" + productTypeId);
                GROUP_BY("product_id,product_name");
            }}.toString();
        return sql;
    }

    public static String queryCusBrowse(Long customerId, Integer offset, Integer pageSize) {
        String sql;
        // 情况1：分页查询客户浏览总时长
        if (customerId == null) {
            sql = new SQL() {{
                SELECT("customer_id,customer_name,customer_icon,SUM(product_browse_time) product_browse_time");
                FROM("mk_product_customer_bp_log");
                GROUP_BY("customer_id,customer_name,customer_icon");
                ORDER_BY("product_browse_time DESC");
                LIMIT(pageSize);
                OFFSET(offset);
            }}.toString();
        }
        // 情况2：查询某个客户浏览每个产品分类总时长
        else {
            sql = new SQL() {{
                SELECT("product_type_id,product_type_name,SUM(product_browse_time) product_browse_time");
                FROM("mk_product_customer_bp_log");
                WHERE("customer_id=" + customerId);
                GROUP_BY("product_type_id,product_type_name");
            }}.toString();
        }
        return sql;
    }
}
