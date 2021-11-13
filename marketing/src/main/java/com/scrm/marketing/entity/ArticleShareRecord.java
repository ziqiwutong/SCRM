package com.scrm.marketing.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fzk
 * @date 2021-10-14 17:32
 * <p>
 * 每篇文章下每个分享者
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("mk_article_share_record")
public class ArticleShareRecord {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long articleId;
    private Long shareId;

    @Deprecated(since = "2021-11-13", forRemoval = true)
    private Boolean showShareFlag;

    /**
     * 阅读记录，存JSON串
     */
    private String readRecord;

    /**
     * 表修改记录属性
     */
    private String createTime;
    private String updateTime;
}
