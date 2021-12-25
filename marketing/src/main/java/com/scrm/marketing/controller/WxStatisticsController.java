package com.scrm.marketing.controller;

import com.scrm.marketing.entity.WxReadRecord;
import com.scrm.marketing.service.WxStatisticsService;
import com.scrm.marketing.util.resp.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 对微信用户阅读做统计分析的controller
 *
 * @author fzk
 * @date 2021-12-25 12:06
 */
@RestController
@RequestMapping("/mk/statistics/wx")
public class WxStatisticsController {

    @Resource
    private WxStatisticsService wxStatisticsService;

    @GetMapping("/articleRead")
    public Result getWxArticleRead(
            @RequestParam(name = "wid", required = false) Long wid,
            @RequestParam(name = "sevenFlag", defaultValue = "true") boolean sevenFlag,
            @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(name = "pageSize", defaultValue = "20") int pageSize
    ) {
        // 目前这个先直接分页查询所有微信用户总的阅读情况
        // 或者某个微信用户7天或者30天的阅读情况，至于读了哪一篇文章再细作打算

        List<WxReadRecord> wxReadRecords = wxStatisticsService.queryWxRead(wid,pageNum, pageSize, sevenFlag);
        return Result.success(wxReadRecords);
    }
}
