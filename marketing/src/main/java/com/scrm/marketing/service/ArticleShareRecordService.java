package com.scrm.marketing.service;

import com.scrm.marketing.util.resp.Result;
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
}
