package com.scrm.marketing.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 每篇文章下每个分享者
 *
 * @author fzk
 * @date 2021-10-14 17:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("mk_article_share_record")
@Deprecated(since = "2021-11-28",forRemoval = true)
public class ArticleShareRecord {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long articleId;
    private Long shareId;

    /**
     * 阅读记录，存JSON串
     */
    private String readRecord;// 默认是"[]"
    private String openids;// 不同的微信openid集合JSON串 , 默认是"[]"
    private Integer readTimes;// 阅读次数
    private Integer readPeople;// 阅读人数

    /**
     * 表修改记录属性
     */
    private String createTime;
    private String updateTime;
}
