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
    Result getArticleDetail(Long id, Long shareId) throws MyException;

    PageResult queryPage(int pageNum, int pageSize, Integer examineFlag, Integer materialType);

    void insert(Article article, @NonNull Long loginId) throws MyException;

    void update(Article article, Long loginId) throws MyException;

    void delete(@NonNull Long id) throws MyException;

    void examine(@NonNull Long id, @NonNull Long loginId, @NonNull Integer examineFlag, String examineNotes) throws MyException;

    Result queryArticleRead(Long articleId, Boolean sevenFlag, Integer pageNum, Integer pageSize);

    List<Article> queryByTitle(String title);
}
