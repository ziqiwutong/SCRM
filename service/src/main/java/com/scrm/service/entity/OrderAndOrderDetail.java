package com.scrm.service.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

/**
 * @author Ganyunhui
 * @create 2021-10-20 21:10
 */
@Data
public class OrderAndOrderDetail extends OrderDetail{

    @JsonAlias({"orderType"})
    private boolean payFlag;
    private String orderStaff;
    private String orderNum;//订单号

}
