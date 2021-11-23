package com.scrm.service.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("se_customer_customized_field")
public class CustomerCustomizedField {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String fieldName;

    private String fieldType;

    private Long departmentId;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String createTime;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String updateTime;
}
