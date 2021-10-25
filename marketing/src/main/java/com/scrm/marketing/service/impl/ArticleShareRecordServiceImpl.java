package com.scrm.marketing.service.impl;

import com.scrm.marketing.entity.ArticleShareRecord;
import com.scrm.marketing.entity.User;
import com.scrm.marketing.mapper.ArticleShareRecordMapper;
import com.scrm.marketing.mapper.UserMapper;
import com.scrm.marketing.service.ArticleShareRecordService;
import com.scrm.marketing.util.resp.Result;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author fzk
 * @date 2021-10-19 23:43
 */
@Service
public class ArticleShareRecordServiceImpl implements ArticleShareRecordService {
    @Resource
    private ArticleShareRecordMapper articleShareRecordMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public Result queryShareRecord(Long articleId, @Nullable List<Long> shareIds) {
        // 1、先查出阅读记录
        List<ArticleShareRecord> articleShareRecords = articleShareRecordMapper.selectByAidAndSids(articleId, shareIds);
        return Result.success(articleShareRecords);
    }

    @Override
    public Result querySharePerson(@NonNull Long articleId) {
        // 根据文章id查询分享人列表
        List<User> sharePersons=userMapper.querySharePerson(articleId);
        return Result.success(sharePersons);
    }
}
