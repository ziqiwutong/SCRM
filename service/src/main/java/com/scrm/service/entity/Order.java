package com.scrm.service.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Ganyunhui
 * @create 2021-10-20 21:01
 */
@Data
@TableName("se_order")
public class Order {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String orderNum;

    private String generateTime;

    private Long customerId;

    private String customerName;

    private String orderStaff;

    private BigDecimal originPrice;

    private BigDecimal changePrice;

    private BigDecimal lastPrice;

    private BigDecimal receivedAmount;

    private String payTime;

    private String saleChannel;

    private String orderSource;

    private Integer orderStatus;

    @TableField(exist = false)
    private List<OrderProduct> productList;

    @TableField(exist = false)
    private Integer productCount;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String createTime;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String updateTime;
}
