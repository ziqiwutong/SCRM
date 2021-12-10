package com.scrm.marketing.share.iuap;

import lombok.Data;

/**
 * @author fzk
 * @date 2021-12-10 22:29
 */
@Data
public class IuapUser {
    private String id;
    private String name;
    private String departmentId;
    private String duty;// 职责
    private String dutyId;//职责id
    private String workPhoto;//工作照
    private String officeDesk;// 工作地点
    private String staffNo;//工号

    private String updateTime;//修改时间（秒级时间戳,10位）,毫秒级别是13位

    private Integer isAdmin;//是否是管理员，不是为0
    private String mobile;//手机号
    private String email;
    private String avatar;//头像
    private String sex;
    private String birthday;
    /*以下为非用友获取的附加信息，查询列表时为空，根据ID查询时才会加上*/
    private String weimobId;//微盟id
}
