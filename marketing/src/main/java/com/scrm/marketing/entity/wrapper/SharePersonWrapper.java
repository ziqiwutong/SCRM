package com.scrm.marketing.entity.wrapper;

import com.scrm.marketing.share.iuap.IuapUser;
import lombok.Data;

/**
 * 包装某篇文章下的某个分享者分享文章的数据
 *
 * @author fzk
 * @date 2021-12-21 18:35
 */
@Data
public class SharePersonWrapper {
    private IuapUser iuapUser;//分享者
    private Long articleId;// 文章id
    private String shareId;// 分享者id
    private long readTimeSum;// 阅读总时长
    private long readTimes;// 阅读次数
}
