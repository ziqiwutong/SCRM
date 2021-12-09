package com.scrm.manage.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("cms_department")
public class Department {

    @TableId
    private Integer id;

    private Integer parentId;

    private Integer leaderMemberId;

    private String departmentName;

    private String subName;

    private String type;

    private Integer memberCount;

    private Integer haveSub;

    @TableField(exist = false)
    private List<Department> children;

    private String message;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String createTime;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String updateTime;
}
