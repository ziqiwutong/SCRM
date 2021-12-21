package com.scrm.marketing.service.impl;

import com.scrm.marketing.entity.*;
import com.scrm.marketing.entity.wrapper.WxReadRecordWrapper;
import com.scrm.marketing.exception.MyException;
import com.scrm.marketing.mapper.*;
import com.scrm.marketing.service.WxReadRecordService;
import com.scrm.marketing.share.iuap.IuapClient;
import com.scrm.marketing.share.iuap.IuapUser;
import com.scrm.marketing.util.MyAssert;
import com.scrm.marketing.util.MyDateTimeUtil;
import com.scrm.marketing.util.MyJsonUtil;
import com.scrm.marketing.util.resp.CodeEum;
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
public class WxReadRecordServiceImpl implements WxReadRecordService {
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
    @Resource
    private IuapClient iuapClient;

    private static final String readRecordKeyPrefix = "wxReadRecord_articleId:";
    private static final String sharePersonKeyPrefix = "sharePerson_articleId:";
    private static final Duration cacheTime = Duration.ofMinutes(10); // 默认10分钟

    /*判断是否需要缓存*/
    private boolean isCache(List<String> shareIds, int pageNum, int pageSize) {
        // 只缓存前3页 且页大小为20
        if (pageNum > 3 || pageSize != 20) return false;
        // 只缓存查询所有分享者 或 查询单个分享者
        return shareIds == null || shareIds.size() < 2;
    }

    /*生成key的方法*/
    private String generateKey(Long articleId, List<String> shareIds, int pageNum) {
        String key;
        if (shareIds == null || shareIds.size() == 0)
            key = readRecordKeyPrefix + articleId + "_page:" + pageNum;
        else
            key = readRecordKeyPrefix + articleId + "_shareId:" + shareIds.get(0) + "_page:" + pageNum;
        return key;
    }


    private WxReadRecordWrapper getCache(Long articleId, List<String> shareIds, int pageNum, int pageSize) {
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

    private void setCache(Long articleId, List<String> shareIds, int pageNum, int pageSize, WxReadRecordWrapper wrapper) {
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

    private void delCache(Long articleId, String shareId) {
        List<String> shareIds = List.of(shareId);
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
    public WxReadRecordWrapper queryShareRecord(Long articleId, List<String> shareIds, int pageNum, int pageSize) {
        // 1.先查出阅读记录
        // 1.1 先去缓存查
        WxReadRecordWrapper cache = getCache(articleId, shareIds, pageNum, pageSize);
        if (cache != null) return cache;

        // 1.2 缓存没查到，查数据库
        int offset = (pageNum - 1) * pageSize;
        List<WxReadRecord> wxReadRecords =
                wxReadRecordMapper.queryByAidAndSids(articleId, shareIds, offset, pageSize);// 对于shareIds的处理交由SQL构造器

        // 2.再查询微信用户信息进行填充
        Map<Long, WxUser> map = new HashMap<>(wxReadRecords.size(), 1.0f);
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
    public List<IuapUser> querySharePerson(Long articleId) {
        // 1.查缓存
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        String json = opsForValue.get(sharePersonKeyPrefix + articleId);
        if (json != null) {
            return MyJsonUtil.toBeanList(json, IuapUser.class);
        }
        // 2.缓存失效，查数据库
        // 根据文章id查询分享人列表,这个玩意也得放缓存哦
        List<String> shareIds = wxReadRecordMapper.queryShareIds(articleId);

        List<IuapUser> iuapUsers = new ArrayList<>(shareIds.size());
        for (String shareId : shareIds) {
            IuapUser iuapUser = iuapClient.getUserById(shareId);
            if (iuapUser != null) {
                iuapUsers.add(iuapUser);
            }
        }

        // 3.放入缓存
        opsForValue.set(sharePersonKeyPrefix + articleId, MyJsonUtil.toJsonStr(iuapUsers), cacheTime);
        return iuapUsers;
    }

    @Override
    @Transactional// 开启事务
    public void addReadRecord(long articleId, String shareId, String openid, int readTime) {
        MyAssert.notNull("openid can not be null", openid);

        /*
        1.判断openid是否合法，取出对应的微信用户表的id
        2.如果是我们的客户，那么：文章客户阅读记录处理：表 mk_article_customer_read
            2.1 今天没读，即没有记录，则插入一条
            2.2 今天读了，有记录，将阅读时间相加
        3.文章阅读记录处理，表：mk_article
        4.微信用户表处理：如果是客户，则更新readerStatus信息
        5.微信阅读记录表处理：直接新增阅读记录
        6.删除缓存
         */
        // 1.判断openid
        List<WxUser> wxUsers = wxUserMapper.selectByOpenid(openid);
        if (wxUsers.size() == 0)
            throw new MyException(CodeEum.CODE_PARAM_ERROR, "openid 不存在");
        WxUser wxUser = wxUsers.get(0);


        // 2.文章客户阅读记录处理
        // 找一找这个openid是不是我们客户
        Long cusId = customerMapper.queryIdByOpenid(openid);
        String customerStatus = null;
        String nowDate = MyDateTimeUtil.getNowDate();
        // 如果是我们的客户
        if (cusId != null) {
            // 2.1 先查询客户状态
            customerStatus = customerMapper.queryCusStatusById(cusId);

            // 2.2 找找今天是否已经有读过了
            List<Long> artCusReadIds = artCusReadMapper
                    .queryTodayRead(articleId, cusId, nowDate);

            // 2.2.1 今天没读，即没有记录，则插入一条
            if (artCusReadIds.size() == 0) {
                ArticleCustomerRead articleCustomerRead = new ArticleCustomerRead();
                articleCustomerRead.setArticleId(articleId);
                articleCustomerRead.setCustomerId(cusId);
                articleCustomerRead.setReadDate(nowDate);
                articleCustomerRead.setReadTime(readTime);

                artCusReadMapper.insert(articleCustomerRead);
            }
            // 2.2.2 今天读了，有记录，将阅读时间相加
            else {
                long artCusReadId = artCusReadIds.get(0);
                artCusReadMapper.addReadTime(artCusReadId, readTime);
            }
        }

        // 3.文章阅读记录处理
        articleMapper.addArticleRead(articleId, readTime);

        // 4.微信用户表处理：更新readerStatus状态信息
        if (customerStatus != null)
            wxUserMapper.updateReaderStatus(wxUser.getId(), customerStatus);

        // 5.微信阅读记录表处理：直接新增阅读记录
        WxReadRecord wxReadRecord = new WxReadRecord(
                articleId,
                shareId,
                wxUser.getId(),
                openid,
                nowDate,
                readTime);
        // 直接插入，不做相同读者当日阅读时间合并
        wxReadRecordMapper.insert(wxReadRecord);

        // 6.删除缓存
        delCache(articleId, shareId);
    }

}
