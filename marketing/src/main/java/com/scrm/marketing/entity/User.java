package com.scrm.marketing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.lang.Nullable;

/**
 * @author fzk
 * @date 2021-10-15 20:55
 */
@Data
@TableName("cms_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;
    private String userIcon;

    private Long departmentId;
    private String telephone;


    /**
     * 表修改记录属性
     */
    private String createTime;
    private String updateTime;
}
