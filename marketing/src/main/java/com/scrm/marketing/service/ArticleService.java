package com.scrm.marketing.service;

import com.scrm.marketing.util.resp.PageResult;
import com.scrm.marketing.util.resp.Result;

/**
 * @author fzk
 * @date 2021-10-14 20:24
 */
public interface ArticleService {
    Result getArticleDetail(Long id);

    PageResult queryPage(int pageNum, int pageSize);
}
