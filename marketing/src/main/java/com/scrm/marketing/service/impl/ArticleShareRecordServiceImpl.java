package com.scrm.marketing.service.impl;

import com.scrm.marketing.entity.ArticleShareRecord;
import com.scrm.marketing.entity.User;
import com.scrm.marketing.mapper.ArticleShareRecordMapper;
import com.scrm.marketing.mapper.UserMapper;
import com.scrm.marketing.service.ArticleShareRecordService;
import com.scrm.marketing.util.resp.Result;
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
        // 2、再查出分享者
        List<User> users = new ArrayList<>();
        for (ArticleShareRecord articleShareRecord : articleShareRecords) {
            User user = userMapper.selectById(articleShareRecord.getShareId());
            if (user != null)
                users.add(user);
        }
        Map<String, Object> map = new HashMap<>(4);
        map.put("articleShareRecords", articleShareRecords);
        map.put("sharePerList", users);
        return Result.success(map);
    }
}
