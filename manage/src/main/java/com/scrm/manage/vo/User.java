package com.scrm.manage.vo;

import lombok.Data;

@Data
public class User {

    private String id; // 成员id，对应member_id

    private String name; // 名称

    private String departmentId; // 部门id

    private String duty; // 职务

    private String dutyId; // 职务id

    private String workPhoto; // 工作照

    private String officeDesk; // 工作地点

    private String staffNo; // 工号

    private String updateTime; // 修改时间

    private Integer isAdmin; // 是否是管理员

    private String mobile; // 手机号

    private String email; // 邮箱

    private String avatar; // 头像

    private String sex; // 性别

    private String birthday; // 生日

    /** 以下为本系统数据库信息 */
    private Long weimobId;
}
