package com.scrm.marketing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author fzk
 * @date 2021-10-20 21:12
 */
@Data
@TableName("se_product_type")
public class ProductType {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String typeName;
    private Integer productNumber;
    /**
     * 表修改记录属性
     */
    private String createTime;
    private String updateTime;
}
