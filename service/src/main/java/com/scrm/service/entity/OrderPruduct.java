package com.scrm.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author Ganyunhui
 * @create 2021-11-03 20:09
 */
@Data
@TableName("se_order_product")
public class OrderPruduct {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    @JsonAlias({"productPic"})
    private String productImage;
    @JsonAlias({"productBuyAmount"})
    private int productAmount;
    private BigDecimal originPrice;
    @JsonAlias({"priceChange"})
    private BigDecimal changePrice;
    private BigDecimal lastPrice;
    private BigDecimal receivedAmount;
    @JsonAlias({"notes"})
    private String orderNotes;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updateTime;
}
