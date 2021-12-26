package com.scrm.marketing.service.impl;

import com.scrm.marketing.entity.WxReadRecord;
import com.scrm.marketing.mapper.WxReadRecordMapper;
import com.scrm.marketing.service.WxStatisticsService;
import com.scrm.marketing.util.MyJsonUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

/**
 * @author fzk
 * @date 2021-12-25 12:19
 */
@Service
public class WxStatisticsServiceImpl implements WxStatisticsService {
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private WxReadRecordMapper wxReadRecordMapper;
    private static final Duration cacheTime = Duration.ofMinutes(20);

    @Override
    public List<WxReadRecord> queryWxRead(Long wid, int pageNum, int pageSize, boolean sevenFlag) {
        // 情况1：wid为null，分页查询所有微信用户总阅读时长
        if (wid == null) {
            // 1.查缓存
            List<WxReadRecord> allWxReads = getPageWxReadCache(pageNum, pageSize);

            if (allWxReads == null) {
                // 2.从数据库查
                // 计算偏移量
                int offset = (pageNum - 1) * pageSize;
                allWxReads = wxReadRecordMapper.queryAllWxRead(offset, pageSize);

                // 3.放入缓存
                setPageWxReadCache(pageNum, pageSize, allWxReads);
            }

            return allWxReads;
        }
        // 情况2：wid不为null，查询此微信用户7天或者30天的阅读时长
        else {
            // 1.查缓存
            List<WxReadRecord> oneWxReads = getOneWxReadCache(wid, sevenFlag);
            if (oneWxReads == null) {
                // 2,查数据库
                LocalDate startDate = LocalDate.now().minusDays(sevenFlag ? 7L : 30L);
                oneWxReads = wxReadRecordMapper.queryOneWxRead(wid, startDate);

                // 3.放入缓存
                setOneWxReadCache(wid, sevenFlag, oneWxReads);
            }
            return oneWxReads;
        }
    }

    private boolean isCache(int pageNum, int pageSize) {
        // 缓存前3页，每页20条
        return 0 < pageNum && pageNum <= 3 && pageSize == 20;
    }

    private List<WxReadRecord> getPageWxReadCache(int pageNum, int pageSize) {
        if (isCache(pageNum, pageSize)) {
            String json = redisTemplate.opsForValue().get("statistics_wx_read_all_pageNum:" + pageNum);
            if (json != null) return MyJsonUtil.toBeanList(json, WxReadRecord.class);
        }
        return null;
    }

    private void setPageWxReadCache(int pageNum, int pageSize, List<WxReadRecord> pageWxRead) {
        if (isCache(pageNum, pageSize)) {
            redisTemplate.opsForValue().set(
                    "statistics_wx_read_all_pageNum:" + pageNum,
                    MyJsonUtil.toJsonStr(pageWxRead),
                    cacheTime
            );
        }
    }

    private List<WxReadRecord> getOneWxReadCache(Long wid, boolean sevenFlag) {
        String json = redisTemplate.opsForValue().get(
                "statistics_wx_read_wid:" + wid + "_sevenFlag:" + sevenFlag);
        if (json != null) return MyJsonUtil.toBeanList(json, WxReadRecord.class);
        return null;
    }

    private void setOneWxReadCache(Long wid, boolean sevenFlag, List<WxReadRecord> oneWxReads) {
        redisTemplate.opsForValue().set(
                "statistics_wx_read_wid:" + wid + "_sevenFlag:" + sevenFlag,
                MyJsonUtil.toJsonStr(oneWxReads),
                cacheTime
        );
    }
}
