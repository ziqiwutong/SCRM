package com.scrm.service.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Ganyunhui
 * @create 2021-10-20 18:59
 */
@Data
@TableName("se_product")
public class Product {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long typeId;

    private String sourceId;

    private String productName;

    private String typeName;

    private String productImage;

    private BigDecimal productPrice;

    private Integer productViewTimes;

    private Integer productSales;

    private Integer productInventory;

    private String productIntro;

    private String brand;

    private String certificate;

    private BigDecimal retailPrice;

    private BigDecimal wholesalePrice;

    private String priceDescribe;

    private Long articleId;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String createTime;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String updateTime;
}
