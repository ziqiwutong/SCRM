package com.scrm.marketing.controller;

import com.scrm.marketing.service.ArticleShareRecordService;
import com.scrm.marketing.util.resp.CodeEum;
import com.scrm.marketing.util.resp.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fzk
 * @date 2021-10-19 23:29
 */
@RestController
@RequestMapping("/mk/article")
public class WxReadRecordController {
    @Resource
    private ArticleShareRecordService articleShareRecordService;

    @GetMapping(path = "/shareRecord")
    public Result queryShareRecord(
            @RequestParam("articleId") Long articleId,
            @RequestParam(name = "shareId", required = false) List<Long> shareIds,
            @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") int pageSize
    ) {
        // 参数检查
        if (articleId == null || articleId < 1L) return Result.PARAM_ERROR();
        if (pageNum < 1 || pageSize < 1) return Result.PARAM_ERROR();

        // 对于shareIds的处理交由SQL构造器
        return Result.success(
                articleShareRecordService.queryShareRecord(articleId, shareIds, pageNum, pageSize)
        );
    }

    @GetMapping(path = "/sharePerson")
    public Result querySharePerson(
            @RequestParam("articleId") Long articleId
    ) {
        // 1.参数检查
        if (articleId == null || articleId < 1)
            return Result.PARAM_ERROR();
        // 调用service
        return articleShareRecordService.querySharePerson(articleId);
    }

    @PostMapping(path = "/addReadRecord")
    public Result addReadRecord(
            @RequestParam("articleId") long articleId,
            @RequestParam("shareId") long shareId,
            @RequestParam("readTime") int readTime,
            @RequestParam("openid") String openid) {
        // 1.参数检查：微信openid,readTime,articleId,shareId
        if (openid == null || "".equals(openid))
            return Result.error(CodeEum.PARAM_MISS, "openid");

        // 2.调用service
        articleShareRecordService.addReadRecord(articleId,shareId,openid,readTime);
        return Result.success();
    }
}
