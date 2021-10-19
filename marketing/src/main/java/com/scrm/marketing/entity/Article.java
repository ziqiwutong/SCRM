package com.scrm.marketing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * @author fzk
 * @date 2021-10-14 16:38
 * 文章表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("mk_article")
public class Article {
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 作者相关属性
     */
    private Long authorId;
    private String authorName;

    /**
     * 文章相关属性
     */
    @Nullable
    private Long productId;
    private String articleTitle;
    private String articleContext;
    private String articleImage;
    private Integer articleViewTimes;
    private Long articleReadTimeSum;

    /**
     * 审核人相关属性
     */
    private Long examineId;
    private Integer examineFlag;
    private String examineName;
    private String examineNotes;

    /**
     * 表修改记录属性
     */
    private String createTime;
    private String updateTime;
}

