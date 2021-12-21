package com.scrm.marketing.controller;

import com.scrm.marketing.service.ArtStatisticsService;
import com.scrm.marketing.util.resp.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author fzk
 * @date 2021-12-21 17:33
 */
@RestController
@RequestMapping("/mk/statistics")
public class ArtStatisticsController {
    @Resource
    private ArtStatisticsService artStatisticsService;

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
    @GetMapping(path = "/articleRead")
    public Result queryArticleRead(
            @RequestParam(name = "articleId", required = false) Long articleId,
            @RequestParam(name = "sevenFlag", required = false) Boolean sevenFlag,
            @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize
    ) {
        // 1.参数检查
        if (articleId == null) {
            if (pageNum < 1 || pageSize < 1)
                return Result.PARAM_ERROR();
        } else if (sevenFlag == null)
            return Result.PARAM_ERROR();
        // 2.调用service
        return artStatisticsService.queryArticleRead(articleId, sevenFlag, pageNum, pageSize);
    }

    @GetMapping(path = "/artSharePerson")
    public Result queryArtSharePerson(@RequestParam("articleId") long articleId) {
        return Result.success(
                artStatisticsService.queryArtSharePerson(articleId));
    }
}
