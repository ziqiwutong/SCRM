package com.scrm.service.dao.sqlprovider;

import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

public class CustomerSqlProvider implements ProviderMethodResolver {
    public String queryIdByBusinessTime(String start, String end) {
        SQL sql = new SQL();
        sql.SELECT_DISTINCT("customer_id");
        sql.FROM("se_business_opportunity");
        if (start != null && end != null) {
            sql.WHERE("create_time between '" + start + "' and' " + end + "'");
        }
        return sql.toString();
    }
}
