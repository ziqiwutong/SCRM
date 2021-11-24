package com.scrm.marketing.service.impl;

import com.alibaba.fastjson.JSON;
import com.scrm.marketing.entity.ArticleCustomerRead;
import com.scrm.marketing.entity.ArticleShareRecord;
import com.scrm.marketing.entity.wrapper.ArticleShareRecordWrapper;
import com.scrm.marketing.entity.User;
import com.scrm.marketing.exception.MyException;
import com.scrm.marketing.mapper.*;
import com.scrm.marketing.service.ArticleShareRecordService;
import com.scrm.marketing.util.MyAssert;
import com.scrm.marketing.util.MyDateTimeUtil;
import com.scrm.marketing.util.MyJsonUtil;
import com.scrm.marketing.util.resp.CodeEum;
import com.scrm.marketing.util.resp.Result;
import com.scrm.marketing.share.wx.WxUserInfoResult;
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
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private ArtCusReadMapper artCusReadMapper;

    @Override
    public Result queryShareRecord(Long articleId, @Nullable List<Long> shareIds) {
        /*
         *  1.先查询出阅读记录
         *  2.通过openid去重，找到不同阅读人数
         */
        // 1、先查出阅读记录
        List<ArticleShareRecord> articleShareRecords = articleShareRecordMapper.selectByAidAndSids(articleId, shareIds);

        ArticleShareRecordWrapper wrapper;
        // 这两种情况不需要去重
        if (articleShareRecords == null || articleShareRecords.size() == 0)
            wrapper = new ArticleShareRecordWrapper(new ArrayList<>(), 0, 0);

        else if (articleShareRecords.size() == 1) {
            ArticleShareRecord shareRecord = articleShareRecords.get(0);
            wrapper = new ArticleShareRecordWrapper(articleShareRecords,
                    shareRecord.getReadTimes(), shareRecord.getReadPeople());
        }
        // 2、其余情况需要去重：
        else {
            int readTimes = 0, readPeople;
            Set<String> set = new HashSet<>(articleShareRecords.size() << 3);
            for (ArticleShareRecord shareRecord : articleShareRecords) {
                readTimes += shareRecord.getReadTimes();// readTimes可以直接+

                String openidList_json = shareRecord.getOpenids();
                if (openidList_json == null || openidList_json.length() == 0 || "[]".equals(openidList_json))
                    continue;
                try {
                    List<String> openidList = JSON.parseArray(openidList_json, String.class);
                    set.addAll(openidList);
                } catch (Exception e) {
                    //e.printStackTrace();
                    //忽略异常
                }
            }
            readPeople = set.size();
            wrapper = new ArticleShareRecordWrapper(articleShareRecords, readTimes, readPeople);
        }
        return Result.success(wrapper);
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
            1.1 处理wxUserInfo,插入阅读时间,插入阅读者状态readerStatus
            1.2 插入到阅读记录字段read_record
            1.3 处理openids字段
            1.4 处理read_times字段、read_people字段
        2.如果是我们的客户，那么：文章客户阅读记录处理：表 mk_article_customer_read
            2.1 今天没读，即没有记录，则插入一条
            2.2 今天读了，有记录，将阅读时间相加
        3.文章阅读记录处理，表：mk_article
         */
        // 1.文章分享阅读记录增加
        List<ArticleShareRecord> articleShareRecords =
                articleShareRecordMapper.selectByAIdAndSid(wxUserInfo.getArticleId(), wxUserInfo.getShareId());
        if (articleShareRecords.size() != 1)
            throw new MyException(CodeEum.CODE_ERROR, "没有找到相应记录，可能是此分享者并未分享过这篇文章，即shareId错误");
        ArticleShareRecord articleShareRecord = articleShareRecords.get(0);

        // 1.0 找一找这个openid是不是我们客户: 不存在则为null
        String customerStatus = customerMapper.queryCusStatusByOpenid(wxUserInfo.getOpenid());

        String newWxRecord;
        try {
            // 1.1.处理readDate和readerStatus
            wxUserInfo.setReadDate(MyDateTimeUtil.getNowDate());
            wxUserInfo.setReaderStatus(customerStatus);
            //System.out.println("======warning：当前并未处理readerStatus====================================");
            newWxRecord = MyJsonUtil.toJsonStr(wxUserInfo);
        } catch (Exception e) {
            //e.printStackTrace();
            return;// 直接返回吧
        }

        // 1.2 插入到阅读记录字段read_record
        String readRecord_json = articleShareRecord.getReadRecord();
        StringBuilder strBuilder = new StringBuilder();
        String newReadRecord_json;
        // 没有阅读记录，new一个
        if (readRecord_json == null || readRecord_json.length() == 0 || "[]".equals(readRecord_json)) {
            strBuilder.append("[");
            strBuilder.append(newWxRecord);
            strBuilder.append("]");
        }
        // 存在阅读记录，加进去
        else {
            char[] chars = readRecord_json.toCharArray();
            strBuilder.append("[");
            strBuilder.append(newWxRecord);
            strBuilder.append(",");
            strBuilder.append(chars, 1, chars.length - 1);
        }
        newReadRecord_json = strBuilder.toString();
        //articleShareRecordMapper.updateReadRecord(wxUserInfo.getArticleId(), wxUserInfo.getShareId(), readRecord_json);

        // 1.3 处理openids字段
        String oldOpenids_json = articleShareRecord.getOpenids();
        String newOpenid = wxUserInfo.getOpenid();
        boolean newOpenidFlag;// 是否新的openid标记
        String newOpenids_json;
        // 没有openid集合，new一个
        if (oldOpenids_json == null || oldOpenids_json.length() == 0 || "[]".equals(oldOpenids_json)) {
            strBuilder = new StringBuilder();
            strBuilder.append("[");
            strBuilder.append("\"");
            strBuilder.append(newOpenid);
            strBuilder.append("\"");
            strBuilder.append("]");
            newOpenids_json = strBuilder.toString();
            newOpenidFlag = true;
        } else {
            List<String> openidList = JSON.parseArray(oldOpenids_json, String.class);
            Set<String> set = new HashSet<>(openidList);
            if (set.add(newOpenid)) {
                newOpenids_json = MyJsonUtil.toJsonStr(set);
                newOpenidFlag = true;
            } else {
                // 说明此openid已经存在
                newOpenidFlag = false;
                newOpenids_json = oldOpenids_json;
            }

        }

        // 1.4 处理read_times字段、read_people字段：交给mapper层处理
        articleShareRecordMapper.addReadRecord(articleShareRecord.getId(), newReadRecord_json, newOpenidFlag, newOpenids_json);

        // 2.文章客户阅读记录处理
        //System.out.println("=====warning: 当前并未处理文章客户阅读记录===============================");
        // 如果是我们的客户
        if (customerStatus != null) {
            // 拿到客户id：必然不会为null吧？这不能给我删了吧
            Long cusId = customerMapper.queryIdByOpenid(wxUserInfo.getOpenid());
            if (cusId != null) {
                String nowDate = MyDateTimeUtil.getNowDate();
                // 先找找今天是否已经有读过了
                List<Long> artCusReadIds = artCusReadMapper
                        .queryTodayRead(wxUserInfo.getArticleId(), cusId, nowDate);

                // 2.1 今天没读，即没有记录，则插入一条
                if (artCusReadIds.size() == 0) {
                    ArticleCustomerRead articleCustomerRead = new ArticleCustomerRead();
                    articleCustomerRead.setArticleId(wxUserInfo.getArticleId());
                    articleCustomerRead.setCustomerId(cusId);
                    articleCustomerRead.setReadDate(nowDate);
                    articleCustomerRead.setReadTime(Long.valueOf(wxUserInfo.getReadTime()));// 默认0

                    artCusReadMapper.insert(articleCustomerRead);
                }
                // 2.2 今天读了，有记录，将阅读时间相加
                else {
                    long artCusReadId = artCusReadIds.get(0);
                    artCusReadMapper.addReadTime(artCusReadId, wxUserInfo.getReadTime());
                }
            }

        }


        // 3.文章阅读记录处理
        articleMapper.addArticleRead(wxUserInfo.getArticleId(), wxUserInfo.getReadTime());
    }
}
