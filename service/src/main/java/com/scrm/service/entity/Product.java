package com.scrm.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Ganyunhui
 * @create 2021-10-20 18:59
 */
@Data
@TableName("se_product")
public class Product {
    @JsonAlias({"productID"})
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long typeId;
    private String productName;
    @JsonAlias({"productType"})
    private String typeName;
    @JsonAlias({"productPic"})
    private String productImage;
    private BigDecimal productPrice;
    private Integer productViewTimes;
    private Integer productSales;
    private Integer productInventory;
    private String productIntro;
    @JsonAlias({"brandIntro"})
    private String brand;
    @JsonAlias({"productCertificate"})
    private String certificate;
    private BigDecimal retailPrice;
    private BigDecimal wholesalePrice;
    private String priceDescribe;
    private String createTime;
    private String updateTime;

}
