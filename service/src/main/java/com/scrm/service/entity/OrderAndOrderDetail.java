package com.scrm.service.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author Ganyunhui
 * @create 2021-10-20 21:10
 */
@Data
public class OrderAndOrderDetail extends Order{

    private Long orderId;
    private Long productId;
    private String productName;
    @JsonAlias({"productPic"})
    private String productImage;
    @JsonAlias({"productBuyAmount"})
    private int productAmount;
    @JsonAlias({"notes"})
    private String orderNotes;


}
