package com.scrm.marketing.controller;

import com.scrm.marketing.entity.Article;
import com.scrm.marketing.service.ArticleService;
import com.scrm.marketing.util.resp.CodeEum;
import com.scrm.marketing.util.resp.PageResult;
import com.scrm.marketing.util.resp.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author fzk
 * @date 2021-10-14 18:11
 */
@RestController
@RequestMapping("/mk/article")
public class ArticleController {
    @Resource
    private ArticleService articleService;

    @GetMapping(path = "/detail")
    public Result getArticleDetail(@RequestParam("id") Long id) {
        if (id == null)
            return Result.error(CodeEum.PARAM_ERROR);
        // 调用service
        return articleService.getArticleDetail(id);
    }

    @GetMapping(path = "/queryPage")
    public PageResult queryPage(
            @RequestParam("pageNum") int pageNum,
            @RequestParam("pageSize") int pageSize
    ) {
        // 参数检查
        if (pageNum < 1 || pageSize < 1)
            return PageResult.error(CodeEum.PARAM_ERROR);
        // 调用service
        return articleService.queryPage(pageNum, pageSize);
    }

//    @PostMapping(path = "/")
//    public Result insert(@RequestBody Article article) {
//        // 检查参数
//        if (article == null)
//            return Result.error(CodeEum.PARAM_MISS);
//
//    }
}
