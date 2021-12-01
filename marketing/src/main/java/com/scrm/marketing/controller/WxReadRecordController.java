package com.scrm.marketing.controller;

import com.scrm.marketing.entity.wrapper.WxReadRecordWrapper;
import com.scrm.marketing.service.ArticleShareRecordService;
import com.scrm.marketing.util.resp.CodeEum;
import com.scrm.marketing.util.resp.Result;
import com.scrm.marketing.share.wx.WxUserInfoResult;
import org.springframework.messaging.handler.annotation.MessageMapping;
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

//    @PostMapping(path = "/addReadRecord")
    public Result addReadRecord(
            @RequestParam("articleId") long articleId,
            @RequestParam("shareId") long shareId,
            @RequestParam("readTime") int readTime,
            @RequestParam("openid") String openid,
            @RequestParam("nickname") String nickname,
            @RequestParam("sex") String sex,
            @RequestParam("province") String province,
            @RequestParam("city") String city,
            @RequestParam("country") String country,
            @RequestParam("headimgurl") String headimgurl,
            @RequestParam("unionid") String unionid) {
        // 1.参数检查：微信openid,readTime,articleId,shareId
        if (openid == null || "".equals(openid))
            return Result.error(CodeEum.PARAM_MISS, "openid");

        WxUserInfoResult wxUserInfo = new WxUserInfoResult();
        wxUserInfo.setArticleId(articleId);
        wxUserInfo.setShareId(shareId);
        wxUserInfo.setReadTime(readTime);
        wxUserInfo.setOpenid(openid);
        wxUserInfo.setNickname(nickname);
        wxUserInfo.setSex(sex);
        wxUserInfo.setProvince(province);
        wxUserInfo.setCity(city);
        wxUserInfo.setCountry(country);
        wxUserInfo.setHeadimgurl(headimgurl);
        wxUserInfo.setUnionid(unionid);

        // 2.调用service
        articleShareRecordService.addReadRecord(wxUserInfo);
        return Result.success();
    }
}
