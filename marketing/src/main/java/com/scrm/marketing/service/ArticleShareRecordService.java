package com.scrm.marketing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.scrm.marketing.util.resp.Result;
import com.scrm.marketing.share.wx.WxUserInfoResult;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @author fzk
 * @date 2021-10-19 23:42
 */
public interface ArticleShareRecordService {
    Result queryShareRecord(Long articleId, @Nullable List<Long> shareIds);

    Result querySharePerson(@NonNull Long articleId);

    void addReadRecord(WxUserInfoResult wxUserInfo) throws JsonProcessingException;
}
