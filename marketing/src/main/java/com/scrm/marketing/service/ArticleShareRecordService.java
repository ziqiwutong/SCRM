package com.scrm.marketing.service;

import com.scrm.marketing.entity.wrapper.WxReadRecordWrapper;
import com.scrm.marketing.util.resp.Result;
import com.scrm.marketing.share.wx.WxUserInfoResult;

import java.util.List;

/**
 * @author fzk
 * @date 2021-10-19 23:42
 */
public interface ArticleShareRecordService {
    WxReadRecordWrapper queryShareRecord(Long articleId, List<Long> shareIds, int pageNum, int pageSize);

    Result querySharePerson(Long articleId);

    void addReadRecord(WxUserInfoResult wxUserInfo);
}
