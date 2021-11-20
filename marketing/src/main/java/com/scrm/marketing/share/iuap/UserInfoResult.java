package com.scrm.marketing.share.iuap;

import lombok.Data;

/**
 * @author fzk
 * @date 2021-11-20 16:34
 */
@Data
public class UserInfoResult {
    private String code;
    private String level;
    private String msg;
    private UserInfo data;


    @Data
    public static class UserInfo {
        /**
         * id系列属性
         */
        private String instance_id; // 实例id
        private String qz_id; // 空间id
        private String tenant_id; // 租户id
        private String member_id; // 成员id
        private String user_id; // NC用户id
        private String yht_userid; // 友户通用户ID

        /**
         * 个人属性
         */
        private String name; // 姓名
        private String avatar; // 用户头像
        private String duty;// 职位
        private String dept_id; // 部门id
        private String sex; // 性别
        private Integer is_admin; // 是否管理员(0:普通用户 1:管理员)
        private String dept_name; // 部门名称
        private String mobile; // 手机号
        private String country_code;
        private String email; // 邮箱
        private long time; //时间戳
        private long expireIn; //时间搓：有效期
        private String staff_no; // 工号
        private Integer status; // 用户状态（1启用0停用）
        private String out_status;
    }
}
