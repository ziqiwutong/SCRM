package com.scrm.marketing.service.impl;

import com.scrm.marketing.entity.*;
import com.scrm.marketing.entity.wrapper.WxReadRecordWrapper;
import com.scrm.marketing.mapper.*;
import com.scrm.marketing.service.ArticleShareRecordService;
import com.scrm.marketing.util.MyAssert;
import com.scrm.marketing.util.MyDateTimeUtil;
import com.scrm.marketing.util.MyJsonUtil;
import com.scrm.marketing.util.resp.Result;
import com.scrm.marketing.share.wx.WxUserInfoResult;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.*;

/**
 * @author fzk
 * @date 2021-10-19 23:43
 */
@Service
public class ArticleShareRecordServiceImpl implements ArticleShareRecordService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private ArtCusReadMapper artCusReadMapper;
    @Resource
    private WxUserMapper wxUserMapper;
    @Resource
    private WxReadRecordMapper wxReadRecordMapper;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    private static final String readRecordKeyPrefix = "wxReadRecord_articleId:";
    private static final Duration cacheTime = Duration.ofMinutes(10); // 默认10分钟

    /*判断是否需要缓存*/
    private boolean isCache(List<Long> shareIds, int pageNum, int pageSize) {
        // 只缓存前3页 且页大小为20
        if (pageNum > 3 || pageSize != 20) return true;
        // 只缓存查询所有分享者 或 查询单个分享者
        return shareIds == null || shareIds.size() < 2;
    }

    /*生成key的方法*/
    private String generateKey(Long articleId, List<Long> shareIds, int pageNum) {
        String key;
        if (shareIds == null || shareIds.size() == 0)
            key = readRecordKeyPrefix + articleId + "_page:" + pageNum;
        else
            key = readRecordKeyPrefix + articleId + "_shareId:" + shareIds.get(0) + "_page:" + pageNum;
        return key;
    }


    private WxReadRecordWrapper getCache(Long articleId, List<Long> shareIds, int pageNum, int pageSize) {
        // 1.判断是否缓存
        if (isCache(shareIds, pageNum, pageSize)) {
            // 2.生成key
            String key = generateKey(articleId, shareIds, pageNum);

            // 3.获取缓存
            ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
            String wxReadRecordWrapper_json = opsForValue.get(key);
            if (wxReadRecordWrapper_json != null)
                return MyJsonUtil.toBean(wxReadRecordWrapper_json, WxReadRecordWrapper.class);
        }
        return null;
    }

    private void setCache(Long articleId, List<Long> shareIds, int pageNum, int pageSize, WxReadRecordWrapper wrapper) {
        // 1.判断是否缓存
        if (isCache(shareIds, pageNum, pageSize)) {
            // 2.生成key
            String key = generateKey(articleId, shareIds, pageNum);

            // 3.生成value, 放入缓存
            String value = MyJsonUtil.toJsonStr(wrapper);

            ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
            opsForValue.set(key, value, cacheTime);
        }
    }

    private void delCache(Long articleId, Long shareId) {
        List<Long> shareIds = List.of(shareId);
        // 1.先删除查询所有分享者的缓存
        String allKey1 = generateKey(articleId, null, 1);
        String allKey2 = generateKey(articleId, null, 2);
        String allKey3 = generateKey(articleId, null, 3);

        // 2.再删除查询 单个分享者的缓存
        String shareKey1 = generateKey(articleId, shareIds, 1);
        String shareKey2 = generateKey(articleId, shareIds, 2);
        String shareKey3 = generateKey(articleId, shareIds, 3);
        // 3.删除操作
        redisTemplate.delete(List.of(allKey1, allKey2, allKey3, shareKey1, shareKey2, shareKey3));
    }

    @Override
    @Transactional
    public WxReadRecordWrapper queryShareRecord(Long articleId, List<Long> shareIds, int pageNum, int pageSize) {
        // 1.先查出阅读记录
        // 1.1 先去缓存查
        WxReadRecordWrapper cache = getCache(articleId, shareIds, pageNum, pageSize);
        if (cache != null) return cache;

        // 1.2 缓存没查到，查数据库
        int offset = (pageNum - 1) * pageSize;
        List<WxReadRecord> wxReadRecords =
                wxReadRecordMapper.queryByAidAndSids(articleId, shareIds, offset, pageSize);// 对于shareIds的处理交由SQL构造器

        // 2.再查询微信用户信息进行填充
        Map<Long, WxUser> map = new HashMap<>(wxReadRecords.size());
        for (WxReadRecord wxReadRecord : wxReadRecords) {
            Long wid = wxReadRecord.getWid();
            WxUser wxUser = map.get(wid);
            if (wxUser == null) {
                // 去数据库查询
                wxUser = wxUserMapper.selectById(wid);
                map.put(wid, wxUser);
            }
            WxReadRecord.fastFillField(wxReadRecord, wxUser);
        }

        // 3.查询阅读次数
        int readTimes = wxReadRecordMapper.queryReadTimes(articleId, shareIds);

        // 4.查询阅读人数
        int readPeople = wxReadRecordMapper.queryReadPeople(articleId, shareIds);

        // 5.包装
        WxReadRecordWrapper wrapper = new WxReadRecordWrapper(readTimes, readPeople, wxReadRecords);

        // 6.放入缓存、返回
        setCache(articleId, shareIds, pageNum, pageSize, wrapper);

        return wrapper;
    }

    @Override
    public Result querySharePerson(Long articleId) {
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
        1.处理wxUserInfo,插入阅读日期,插入阅读者状态readerStatus
        2.如果是我们的客户，那么：文章客户阅读记录处理：表 mk_article_customer_read
            2.1 今天没读，即没有记录，则插入一条
            2.2 今天读了，有记录，将阅读时间相加
        3.文章阅读记录处理，表：mk_article
        4.微信用户表处理：更新用户信息
        5.微信阅读记录表处理：直接新增阅读记录
        6.删除缓存
         */

        // 1. 找一找这个openid是不是我们客户: 取出客户状态，不存在则为null
        String customerStatus = customerMapper.queryCusStatusByOpenid(wxUserInfo.getOpenid());
        wxUserInfo.setReaderStatus(customerStatus);
        String nowDate = MyDateTimeUtil.getNowDate();

        // 2.文章客户阅读记录处理
        // 如果是我们的客户
        if (customerStatus != null) {
            // 拿到客户id：必然不会为null吧？这不能给我删了吧
            Long cusId = customerMapper.queryIdByOpenid(wxUserInfo.getOpenid());
            if (cusId != null) {
                // 先找找今天是否已经有读过了
                List<Long> artCusReadIds = artCusReadMapper
                        .queryTodayRead(wxUserInfo.getArticleId(), cusId, nowDate);

                // 2.1 今天没读，即没有记录，则插入一条
                if (artCusReadIds.size() == 0) {
                    ArticleCustomerRead articleCustomerRead = new ArticleCustomerRead();
                    articleCustomerRead.setArticleId(wxUserInfo.getArticleId());
                    articleCustomerRead.setCustomerId(cusId);
                    articleCustomerRead.setReadDate(nowDate);
                    articleCustomerRead.setReadTime(wxUserInfo.getReadTime());

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

        // 4.微信用户表处理：更新用户信息
        WxUser wxUser = new WxUser(
                wxUserInfo.getOpenid(),
                wxUserInfo.getNickname(),
                wxUserInfo.getSex(),
                wxUserInfo.getProvince(),
                wxUserInfo.getCity(),
                wxUserInfo.getCountry(),
                wxUserInfo.getHeadimgurl(),
                wxUserInfo.getUnionid(),
                wxUserInfo.getReaderStatus());
        // 4.1 查看此openid是否已经存在
        List<WxUser> wxUsers = wxUserMapper.selectByOpenid(wxUser.getOpenid());
        // 4.2 已经存在：更新信息
        if (wxUsers.size() == 1) {
            wxUser.setId(wxUsers.get(0).getId());
            wxUserMapper.updateById(wxUser);
        }
        // 4.3 不存在：插入
        else if (wxUsers.size() == 0) {
            wxUserMapper.insert(wxUser);
        }

        // 5.微信阅读记录表处理：直接新增阅读记录
        WxReadRecord wxReadRecord = new WxReadRecord(
                wxUserInfo.getArticleId(),
                wxUserInfo.getShareId(),
                wxUser.getId(),
                wxUser.getOpenid(),
                nowDate,
                wxUserInfo.getReadTime());
        // 直接插入，不做相同读者当日阅读时间合并
        wxReadRecordMapper.insert(wxReadRecord);

        // 6.删除缓存
        delCache(wxUserInfo.getArticleId(), wxUserInfo.getShareId());
    }

}
