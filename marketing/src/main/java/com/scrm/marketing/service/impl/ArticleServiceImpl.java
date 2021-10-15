package com.scrm.marketing.service.impl;

import com.scrm.marketing.entity.Article;
import com.scrm.marketing.mapper.ArticleMapper;
import com.scrm.marketing.service.ArticleService;
import com.scrm.marketing.util.resp.CodeEum;
import com.scrm.marketing.util.resp.PageResult;
import com.scrm.marketing.util.resp.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fzk
 * @date 2021-10-14 20:24
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private ArticleMapper articleMapper;

    @Override
    public Result getArticleDetail(Long id) {
        Article article = articleMapper.selectById(id);
        if (article == null)
            return Result.error(CodeEum.NOT_EXIST);
        else return Result.success(article);
    }

    @Override
    public PageResult queryPage(int pageNum, int pageSize) {
        int index = (pageNum - 1) * pageSize;
        int total = articleMapper.selectCount(null);
        List<Article> articles=articleMapper.queryPage(index,pageSize);
        return PageResult.success(articles,total,pageNum);
    }
}
