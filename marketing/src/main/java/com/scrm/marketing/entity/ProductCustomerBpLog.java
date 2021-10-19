package com.scrm.marketing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fzk
 * @date 2021-10-14 17:49
 * 每个产品的每个客户的浏览时长以及购买统计表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("mk_product_customer_bp_log")
public class ProductCustomerBpLog {
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 产品相关属性
     */
    private Long productId;
    private String productName;
    private Long productTypeId;
    private String productTypeName;

    /**
     * 客户相关属性
     */
    private Long customerId;
    private String customerName;

    /**
     * 记录的属性
     */
    private Integer purchaseNum;
    private Long productBrowseTime;

    /**
     * 表修改记录属性
     */
    private String createTime;
    private String updateTime;
}
