package com.scrm.service.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("se_label")
public class Label {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String labelType;

    private String labelName;

    private Integer labelObject;

    private String createTime;

    private String updateTime;
}
