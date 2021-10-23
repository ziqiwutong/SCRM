package com.scrm.marketing.controller;

import com.scrm.marketing.service.ArticleShareRecordService;
import com.scrm.marketing.util.resp.CodeEum;
import com.scrm.marketing.util.resp.PageResult;
import com.scrm.marketing.util.resp.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestParam(value = "shareId", required = false) List<Long> shareIds
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
        if(articleId==null||articleId<1)
            return Result.error(CodeEum.PARAM_ERROR);
        // 调用service
        return articleShareRecordService.querySharePerson(articleId);
    }
}
