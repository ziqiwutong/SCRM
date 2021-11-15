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
 * @create 2021-10-20 21:01
 */
@Data
@TableName("se_order")
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;
    @JsonAlias({"orderID"})
    private String orderNum;
    private Long customerId;
    @JsonAlias({"orderBuyer"})
    private String customerName;
    private String orderStaff;
    @JsonAlias({"productPrice"})
    private BigDecimal originPrice;
    @JsonAlias({"priceChange"})
    private BigDecimal changePrice;
    private BigDecimal lastPrice;
    private BigDecimal receivedAmount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp payTime;
    private String saleChannel;
    private String orderSource;
    @JsonAlias({"orderType"})
    private int orderStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updateTime;
}
