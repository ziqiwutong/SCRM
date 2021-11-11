package com.scrm.marketing.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.scrm.marketing.entity.ArticleShareRecord;
import com.scrm.marketing.entity.User;
import com.scrm.marketing.exception.MyException;
import com.scrm.marketing.mapper.ArticleShareRecordMapper;
import com.scrm.marketing.mapper.UserMapper;
import com.scrm.marketing.service.ArticleShareRecordService;
import com.scrm.marketing.util.MyAssert;
import com.scrm.marketing.util.MyDateTimeUtil;
import com.scrm.marketing.util.MyJsonUtil;
import com.scrm.marketing.util.resp.CodeEum;
import com.scrm.marketing.util.resp.Result;
import com.scrm.marketing.wx.WxUserInfoResult;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        List<User> sharePersons = userMapper.querySharePerson(articleId);
        return Result.success(sharePersons);
    }

    /**
     * @param wxUserInfo 微信用户信息，可能是客户，里面必须有：微信openid,readTime,articleId,shareId
     */
    @Override
    @Transactional// 开启事务
    public void addReadRecord(WxUserInfoResult wxUserInfo) {
        MyAssert.notNull("wxUserInfo can not be null", wxUserInfo);

        /*
        1.文章分享阅读记录增加，表：mk_article_share_record
            1.1 处理wxUserInfo,插入阅读时间;插入阅读者状态readerStatus
            1.2 插入到阅读记录字段read_record
        2.如果是我们的客户，那么：
            2.1 文章客户阅读记录处理：表 mk_article_customer_read
         */
        // 1.文章分享阅读记录增加
        List<ArticleShareRecord> articleShareRecords =
                articleShareRecordMapper.selectByAIdAndSid(wxUserInfo.getArticleId(), wxUserInfo.getShareId());
        if (articleShareRecords.size() != 1)
            throw new MyException(CodeEum.CODE_ERROR, "没有找到相应记录，可能是此分享者并未分享过这篇文章，即shareId错误");
        ArticleShareRecord articleShareRecord = articleShareRecords.get(0);

        String newRecord = null;
        try {
            // 1.1.处理readDate和readerStatus
            wxUserInfo.setReadDate(MyDateTimeUtil.getNowDate());
            System.out.println("======warning：当前并未处理readerStatus");
            newRecord = MyJsonUtil.writeToString(wxUserInfo);
        } catch (JsonProcessingException e) {
            //e.printStackTrace();
            return;// 直接返回吧
        }
        // 1.2 插入到阅读记录字段read_record
        String readRecord = articleShareRecord.getReadRecord();
        StringBuilder strBuilder = new StringBuilder();
        // 没有阅读记录，new一个
        if (readRecord == null || "".equals(readRecord) || "[]".equals(readRecord)) {
            strBuilder.append("[");
            strBuilder.append(newRecord);
            strBuilder.append("]");
        }
        // 存在阅读记录，加进去
        else {
            char[] chars = readRecord.toCharArray();
            strBuilder.append("[");
            strBuilder.append(newRecord);
            strBuilder.append(",");
            strBuilder.append(chars, 1, chars.length - 1);
        }
        readRecord = strBuilder.toString();
        articleShareRecordMapper.updateReadRecord(wxUserInfo.getArticleId(),wxUserInfo.getShareId(),readRecord);

        // 2.文章客户阅读记录处理
        System.out.println("=====warning: 当前并未处理文章客户阅读记录");
    }
}
