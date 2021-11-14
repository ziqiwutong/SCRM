package com.scrm.marketing.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.scrm.marketing.service.ArticleShareRecordService;
import com.scrm.marketing.util.resp.CodeEum;
import com.scrm.marketing.util.resp.Result;
import com.scrm.marketing.wx.WxUserInfoResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fzk
 * @date 2021-10-19 23:29
 */
@RestController
@RequestMapping("/mk/article")
public class ArticleShareRecordController {
    @Resource
    private ArticleShareRecordService articleShareRecordService;

    @GetMapping(path = "/shareRecord")
    public Result queryShareRecord(
            @RequestParam("articleId") Long articleId,
            @RequestParam(name="shareId",required = false)List<Long> shareIds
    ) {
        // 参数检查
        if (articleId == null || articleId < 1L)
            return Result.error(CodeEum.PARAM_ERROR);
        // 对于shareIds的处理交由SQL构造器
        return articleShareRecordService.queryShareRecord(articleId, shareIds);
    }

    @GetMapping(path = "/sharePerson")
    public Result querySharePerson(
            @RequestParam("articleId") Long articleId
    ) {
        // 1.参数检查
        if (articleId == null || articleId < 1)
            return Result.error(CodeEum.PARAM_ERROR);
        // 调用service
        return articleShareRecordService.querySharePerson(articleId);
    }

    /**
     * 1.文章分享阅读记录增加，表：mk_article_share_record
     * 2.如果是我们的客户，那么：
     * 2.1 文章客户阅读记录处理：表 mk_article_customer_read
     *
     * @param wxUserInfo 微信用户信息、文章id，shareId
     * @return result
     */
    @PostMapping(path = "/addReadRecord")
    public Result addReadRecord(@RequestBody WxUserInfoResult wxUserInfo) throws JsonProcessingException {
        // 1.参数检查：微信openid,readTime,articleId,shareId
        if (wxUserInfo == null || wxUserInfo.getOpenid() == null || wxUserInfo.getReadTime() == null)
            return Result.PARAM_MISS();
        if (wxUserInfo.getArticleId() == null || wxUserInfo.getShareId() == null)
            return Result.PARAM_MISS();

        // 2.调用service
        articleShareRecordService.addReadRecord(wxUserInfo);
        return Result.success();
    }
}
