package com.scrm.marketing.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.scrm.marketing.entity.Article;
import com.scrm.marketing.exception.MyException;
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
    @SaCheckLogin
    public Result getArticleDetail(
            @RequestParam("id") Long id,
            @RequestParam(value = "shareId", required = false) Long shareId) throws MyException {
        if (id == null)
            return Result.error(CodeEum.PARAM_ERROR);
        // 调用service
        return articleService.getArticleDetail(id, shareId);
    }

    @GetMapping(path = "/queryPage")
    public PageResult queryPage(
            @RequestParam("pageNum") int pageNum,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("examineFlag") Integer examineFlag
    ) {
        // 参数检查
        if (pageNum < 1 || pageSize < 1)
            return PageResult.error(CodeEum.PARAM_ERROR);
        // 调用service
        return articleService.queryPage(pageNum, pageSize, examineFlag);
    }

    @PostMapping(path = "/insert")
    @SaCheckPermission("article-add")//拥有文章增加权限
    public Result insert(@RequestBody Article article) throws MyException {
        // 检查参数
        if (article == null)
            return Result.error(CodeEum.PARAM_MISS);

        // 获取loginId
        Long loginId = Long.parseLong(StpUtil.getLoginId().toString());
        // 调用service
        articleService.insert(article, loginId);
        return Result.success();
    }

    @PutMapping(path = "/update")
    @SaCheckPermission("article-update")
    public Result update(@RequestBody Article article) throws MyException {
        // 1、检查参数
        if (article == null || article.getId() == null)
            return Result.error(CodeEum.PARAM_MISS);
        // 2、获取loginId
        Long loginId = Long.parseLong(StpUtil.getLoginId().toString());

        // 3.调用service
        articleService.update(article, loginId);

        return Result.success();
    }

    @DeleteMapping(path = "/delete")
    @SaCheckPermission("article-delete")
    public Result delete(@RequestParam("id") Long id) throws MyException {
        // 1、检查参数
        if (id == null)
            return Result.error(CodeEum.PARAM_MISS);

        // 2、调用service
        articleService.delete(id);
        return Result.success();
    }

    @PutMapping(path = "/examine")
    @SaCheckPermission("article-update")
    public Result examine(
            @RequestParam("id") Long id,
            @RequestParam("examineFlag") Integer examineFlag,
            @RequestParam("examineNotes") String examineNotes
    ) throws MyException {
        // 1.参数检查
        if (id == null || examineFlag == null)
            return Result.error(CodeEum.PARAM_MISS);

        // 2、获取loginId
        Long loginId = Long.parseLong(StpUtil.getLoginId().toString());

        // 3.调用service
        articleService.examine(id, loginId, examineFlag, examineNotes);

        return Result.success();
    }

//    @GetMapping(path="/statistics/read")
//    public Result queryArticleRead(){
//
//    }
}
