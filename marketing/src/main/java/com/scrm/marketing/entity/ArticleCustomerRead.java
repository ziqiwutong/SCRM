package com.scrm.marketing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fzk
 * @date 2021-10-14 17:42
 * <p>
 * 每篇文章每个客户的阅读记录表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("mk_article_customer_read")
public class ArticleCustomerRead {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long articleId;
    private Long customerId;
    private Long readTime;
    private String readDate;

    /**
     * 表修改记录属性
     */
    private String createTime;
    private String updateTime;
}
