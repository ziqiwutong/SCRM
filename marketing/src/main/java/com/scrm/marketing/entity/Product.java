package com.scrm.marketing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author fzk
 * @date 2021-10-20 21:12
 */
@Data
@TableName("se_product")
public class Product {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 分类相关属性
     */
    private Long typeId;
    private String typeName;

    /**
     * 产品相关属性
     */
    private String productName;//产品名
    private String productImage;//产品图片
    private BigDecimal productPrice;// 产品价格
    private Integer productViewTimes;//产品浏览次数
    private Integer productSales;//产品销量
    private Integer productInventory;//产品库存
    private String productIntro;//产品介绍
    private String productLink;//产品链接
    private String productQr;//产品二维码
    private Long sharePersonId;//分享人id；分享人指分享产品的用户，没有则是系统用户


    /**
     * 表修改记录属性
     */
    private String createTime;
    private String updateTime;
}
