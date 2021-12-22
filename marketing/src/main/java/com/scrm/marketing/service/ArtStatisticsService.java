package com.scrm.marketing.service;

import com.scrm.marketing.entity.wrapper.SharePersonWrapper;
import com.scrm.marketing.util.resp.Result;

import java.util.List;


/**
 * @author fzk
 * @date 2021-12-21 17:34
 */
public interface ArtStatisticsService {
    Result queryArticleRead(Long articleId, Boolean sevenFlag, Integer pageNum, Integer pageSize);

    List<SharePersonWrapper> queryArtSharePerson(long articleId);
}
