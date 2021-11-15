package com.scrm.service.dao.sqlprovider;

import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.stream.Collectors;

public class LabelSqlProvider implements ProviderMethodResolver {
    public String deleteCustomerRelations(Long customerId, List<Long> labelIds) {
        SQL sql = new SQL();
        sql.DELETE_FROM("se_customer_label_relation");
        sql.WHERE("customer_id = " + customerId);
        sql.WHERE(
                "label_id in (" +
                labelIds.stream().map(String::valueOf).collect(Collectors.joining(",")) +
                ")"
        );
        return sql.toString();
    }

    public String insertCustomerRelations(Long customerId, List<Long> labelIds) {
        SQL sql = new SQL();
        sql.INSERT_INTO("se_customer_label_relation");
        sql.INTO_COLUMNS("customer_id", "label_id");
        for (int i = 0; i < labelIds.size(); i++) {
            if (i > 0) sql.ADD_ROW();
            sql.INTO_VALUES("" + customerId, "" + labelIds.get(i));
        }
        return sql.toString();
    }
}
