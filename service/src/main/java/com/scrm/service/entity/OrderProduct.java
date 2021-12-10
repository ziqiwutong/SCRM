package com.scrm.service.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Ganyunhui
 * @create 2021-11-03 20:09
 */
@Data
@TableName("se_order_product")
public class OrderProduct {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long orderId;

    private Long productId;

    private String productName;

    private String productImage;

    private Integer productAmount;

    private BigDecimal originPrice;

    private BigDecimal changePrice;

    private BigDecimal lastPrice;

    private BigDecimal receivedAmount;

    private String orderNotes;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String createTime;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String updateTime;
}
