package com.scrm.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;

/**
 * @author Ganyunhui
 * @create 2021-10-20 20:28
 */
@Data
@TableName("se_order_detail")
public class OrderDetail {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;
    @JsonAlias({"orderBuyer"})
    private String customerName;
    private String productName;
    @JsonAlias({"productPic"})
    private String productImage;
    private BigDecimal productPrice;
    @JsonAlias({"productBuyAmount"})
    private Integer productAmount;
    private BigDecimal originPrice;
    @JsonAlias({"priceChange"})
    private BigDecimal changePrice;
    private BigDecimal lastPrice;
    private BigDecimal receivedAmount;
    private String payTime;
    private String orderSource;
    @JsonAlias({"notes"})
    private String orderNotes;
    private String createTime;
    private String updateTime;

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
