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
@TableName("mk_article")
public class Article {
    /**
     * 常量
     */
    public static int ARTICLE_TYPE_ORIGIN = 0;// 原创
    public static int ARTICLE_TYPE_REPRINT = 1;// 转载
    public static int EXAMINE_FLAG_WAIT = 0;//待审核
    public static int EXAMINE_FLAG_ACCESS = 1;//审核通过
    public static int EXAMINE_FLAG_NOT_ACCESS = 2;//审核不通过


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
     * 转载文章相关属性
     */
    private String articleOriginAuthor;
    private String articleAccountName;
    private String articlePower;// 原公众号地址
    private Integer articleType;// 文章类型，0原创，1转载
    /**
     * 表修改记录属性
     */
    private String createTime;
    private String updateTime;
}

