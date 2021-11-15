package com.scrm.manage.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("cms_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String userIcon;

    private Long departmentId;

    private String telephone;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String createTime;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String updateTime;
}
