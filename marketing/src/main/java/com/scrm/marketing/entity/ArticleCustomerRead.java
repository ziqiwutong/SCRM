package com.scrm.marketing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 每篇文章每个客户的阅读记录表
 *
 * @author fzk
 * @date 2021-10-14 17:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("mk_article_customer_read")
@Deprecated(since = "2021-12-21",forRemoval = true)
public class ArticleCustomerRead {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long articleId;
    @TableField(exist = false)
    private String articleTitle;// 文章标题，数据库中没有存储，需要另外查询

    private Long customerId;

    @TableField(exist = false)
    private String customerIcon;// 客户头像，数据库中没有存储，需要另外查询
    @TableField(exist = false)
    private String customerName;// 客户名称，数据库中没有存储，需要另外查询

    private Integer readTime;//单位s
    private String readDate;

    /**
     * 表修改记录属性
     */
    private String createTime;
    private String updateTime;
}
