package com.scrm.service.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("se_customer_relationship")
public class CustomerRelation {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long customerId;

    private String relationType;

    private String relationDetail;

    private Timestamp communicationTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp getCommunicationTime() { return communicationTime; }

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String createTime;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String updateTime;
}
