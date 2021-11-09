package com.scrm.service.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("se_customer_relationship")
public class CustomerRelation {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long customerId;

    private String relationType;

    private String relationDetail;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String createTime;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String updateTime;
}
