package com.scrm.marketing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fzk
 * @date 2021-10-14 17:44
 * <p>
 * 客户阅读记录表，记录客户每天的阅读总时长
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("mk_customer_read_log")
public class CustomerReadLog {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long customerId;
    private Long readTime;
    private String readDate;

    /**
     * 表修改记录属性
     */
    private String createTime;
    private String updateTime;
}
