package com.scrm.marketing.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.scrm.marketing.entity.Article;
import com.scrm.marketing.exception.MyException;
import com.scrm.marketing.service.ArticleService;
import com.scrm.marketing.util.resp.CodeEum;
import com.scrm.marketing.util.resp.Result;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
    //@SaCheckLogin
    public Result getArticleDetail(
            @RequestParam("id") Long id,
            @RequestParam(value = "shareId", required = false) Long shareId) throws MyException {
        if (id == null)
            return Result.error(CodeEum.PARAM_ERROR);
        // 调用service
        return articleService.getArticleDetail(id, shareId);
    }

    @GetMapping(path = "/queryPage")
    public Result queryPage(
            @RequestParam("pageNum") int pageNum,
            @RequestParam("pageSize") int pageSize,
            @RequestParam(name = "examineFlag", required = false) Integer examineFlag,
            @RequestParam(name = "materialType", required = false) Integer materialType
    ) {
        // 参数检查：materialType必须为0或1，examineFlag必须为0,1,2
        if (pageNum < 1 || pageSize < 1)
            return Result.PARAM_ERROR();
        if (materialType != null)
            if (materialType != Article.MATERIAL_TYPE_PERSONAL && materialType != Article.MATERIAL_TYPE_ENTERPRISE)
                return Result.PARAM_ERROR();
        if (examineFlag != null)
            if (examineFlag != Article.EXAMINE_FLAG_WAIT && examineFlag != Article.EXAMINE_FLAG_ACCESS && examineFlag != Article.EXAMINE_FLAG_NOT_ACCESS)
                return Result.PARAM_ERROR();
        // 调用service
        return articleService.queryPage(pageNum, pageSize, examineFlag, materialType);
    }

    @GetMapping(path = "/queryByTitle")
    public Result queryByTitle(
            @RequestParam("title") String title
    ) {
        if (Strings.trimToNull(title) == null)
            return Result.PARAM_ERROR();
        // 调用service
        List<Article> articles = articleService.queryByTitle(title);
        return Result.success(articles);
    }

    @PostMapping(path = "/insert")
    @SaCheckPermission("article-add")//拥有文章增加权限
    public Result insert(@RequestBody Article article) throws MyException {
        // 1.1 检查参数：必须有标题,内容,文章类型,文章背景图
        if (article == null)
            return Result.PARAM_MISS();
        if (article.getArticleTitle() == null || article.getArticleContext() == null || article.getArticleType() == null || article.getArticleImage() == null)
            return Result.PARAM_MISS();

        // 1.2 转载公众号文章相关属性检查
        if (article.getArticleType().equals(Article.ARTICLE_TYPE_REPRINT)) {
            if (article.getArticleOriginAuthor() == null || article.getArticleAccountName() == null || article.getArticlePower() == null)
                return Result.PARAM_MISS();
        }
        // 1.3 参数合法性检查：materialType
        if (article.getMaterialType() != null) {
            int materialType = article.getMaterialType();
            if (materialType != Article.MATERIAL_TYPE_PERSONAL && materialType != Article.MATERIAL_TYPE_ENTERPRISE)
                return Result.PARAM_ERROR();
        }

        // 2.获取loginId
        Long loginId = Long.parseLong(StpUtil.getLoginId().toString());
        // 3.调用service
        articleService.insert(article, loginId);
        return Result.success();
    }

    @PutMapping(path = "/update")
    @SaCheckPermission("article-update")
    public Result update(@RequestBody Article article) throws MyException {
        // 1、检查参数
        if (article == null || article.getId() == null)
            return Result.PARAM_MISS();
        // 1.1 参数合法性检查：materialType,examineFlag,articleType
        if (article.getMaterialType() != null) {
            if (!Article.checkMaterialType(article))
                return Result.PARAM_ERROR();
        }
        if (article.getExamineFlag() != null) {
            if(!Article.checkExamineFlag(article))
                return Result.PARAM_ERROR();
        }
        if(article.getArticleType()!=null){
            if(!Article.checkArticleType(article))
                return Result.PARAM_ERROR();
        }

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
            return Result.PARAM_MISS();

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
        // 1.参数检查：examineFlag必须合法
        if (id == null || examineFlag == null)
            return Result.error(CodeEum.PARAM_MISS);
        if (examineFlag != Article.EXAMINE_FLAG_WAIT && examineFlag != Article.EXAMINE_FLAG_ACCESS && examineFlag != Article.EXAMINE_FLAG_NOT_ACCESS)
            return Result.PARAM_ERROR();

        // 2、获取loginId
        Long loginId = Long.parseLong(StpUtil.getLoginId().toString());

        // 3.调用service
        articleService.examine(id, loginId, examineFlag, examineNotes);

        return Result.success();
    }

    /**
     * 查询文章阅读情况
     * 情况1：无文章id，则分页查询文章总阅读时长，从 mk_article 表查
     * 情况2：有文章id，则查询特定文章的7天或者30天的阅读时长，从 mk_article_customer_read 表查
     *
     * @param articleId 文章id
     * @param sevenFlag 查7天的标记，true查7天，false 查30天
     * @param pageNum   页码
     * @param pageSize  页大小
     * @return 返回Result或者PageResult
     */
    @GetMapping(path = "/statistics/read")
    public Result queryArticleRead(
            @RequestParam(name = "articleId", required = false) Long articleId,
            @RequestParam(name = "sevenFlag", required = false) Boolean sevenFlag,
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "pageSize", required = false) Integer pageSize
    ) {
        // 1.参数检查
        if (articleId == null) {
            if (pageNum == null || pageSize == null)
                return Result.PARAM_MISS();
            else if (pageNum < 1 || pageSize < 1)
                return Result.PARAM_ERROR();
        } else if (sevenFlag == null)
            return Result.PARAM_ERROR();
        // 2.调用service
        return articleService.queryArticleRead(articleId, sevenFlag, pageNum, pageSize);
    }
}
