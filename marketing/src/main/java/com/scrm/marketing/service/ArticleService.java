package com.scrm.marketing.service;

import com.scrm.marketing.entity.Article;
import com.scrm.marketing.exception.MyException;
import com.scrm.marketing.util.resp.PageResult;
import com.scrm.marketing.util.resp.Result;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author fzk
 * @date 2021-10-14 20:24
 */
public interface ArticleService {
    Result getArticleDetail(Long id, String shareId);

    PageResult queryPage(int pageNum, int pageSize, Integer examineFlag, Integer materialType);

    void insert(Article article, String loginId);

    void update(Article article, String loginId);

    void delete(Long id);

    void examine(Long id, String loginId, Integer examineFlag, String examineNotes);

    Result queryArticleRead(Long articleId, Boolean sevenFlag, Integer pageNum, Integer pageSize);

    List<Article> queryByTitle(String title);
}
