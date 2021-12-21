package com.scrm.marketing.service.impl;

import com.scrm.marketing.entity.Article;
import com.scrm.marketing.entity.WxReadRecord;
import com.scrm.marketing.entity.wrapper.SharePersonWrapper;
import com.scrm.marketing.mapper.ArticleMapper;
import com.scrm.marketing.mapper.WxReadRecordMapper;
import com.scrm.marketing.service.ArtStatisticsService;
import com.scrm.marketing.share.iuap.IuapClient;
import com.scrm.marketing.util.MyJsonUtil;
import com.scrm.marketing.util.resp.PageResult;
import com.scrm.marketing.util.resp.Result;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

/**
 * @author fzk
 * @date 2021-12-21 17:35
 */
@Service
public class ArtStatisticsServiceImpl implements ArtStatisticsService {
    // 缓存时间: 20分钟
    private static final Duration cacheTime = Duration.ofMinutes(20L);
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private WxReadRecordMapper wxReadRecordMapper;
    @Resource
    private IuapClient iuapClient;

    @Override
    /*感觉这里应该不需要对productIds属性进行处理*/
    public Result queryArticleRead(Long articleId, Boolean sevenFlag, Integer pageNum, Integer pageSize) {
        // 情况1：无文章id，则分页查询文章总阅读时长，从 mk_article 表查：审核通过，无论哪种materialType
        if (articleId == null) {
            // 1.查缓存
            PageResult result = getAllReadCache(pageNum, pageSize);
            if (result != null) return result;

            // 2.计算偏移量
            int offset = (pageNum - 1) * pageSize;
            // 从mk_article表查询文章总阅读时长
            List<Article> articles = articleMapper.queryPage(offset, pageSize, Article.EXAMINE_FLAG_ACCESS, null);
            // 3.查询文章总数
            int total = articleMapper.queryCount(offset, pageSize, Article.EXAMINE_FLAG_ACCESS, null);
            // 4.放缓存
            result = PageResult.success(articles, total, pageNum);
            setAllReadCache(pageNum, pageSize, result);

            return result;
        }
        // 情况2：有文章id，则查询特定文章的7天或者30天的阅读时长，从 mk_article_customer_read 表查
        else {
            // 1.查缓存
            List<WxReadRecord> artReadRecords =
                    getOneReadCache(articleId, sevenFlag);
            if (artReadRecords != null) return Result.success(artReadRecords);

            // 2.这里根据sevenFlag生成查询起始时间
            LocalDate startDate = LocalDate.now();
            if (sevenFlag) startDate = startDate.minusDays(7L);
            else startDate = startDate.minusDays(30L);   // 查询30天

            // 3.从mk_article_customer_read表查
            artReadRecords = wxReadRecordMapper.queryArticleRead(articleId, startDate);

            // 4.放缓存
            setOneReadCache(articleId, sevenFlag, artReadRecords);

            return Result.success(artReadRecords);
        }
    }

    private boolean isCache(int pageNum, int pageSize) {
        // 缓存前3页，每页20条
        return 0 < pageNum && pageNum <= 3 && pageSize == 20;
    }

    private PageResult getAllReadCache(int pageNum, int pageSize) {
        if (isCache(pageNum, pageSize)) {
            String json = redisTemplate.opsForValue().get("statistics_article_read_all_pageNum:" + pageNum);
            if (json != null)
                return MyJsonUtil.toBean(json, PageResult.class);
        }
        return null;
    }

    private void setAllReadCache(Integer pageNum, Integer pageSize, PageResult result) {
        if (isCache(pageNum, pageSize)) {
            redisTemplate.opsForValue().set("statistics_article_read_all_pageNum:" + pageNum,
                    MyJsonUtil.toJsonStr(result),
                    cacheTime);
        }
    }

    private List<WxReadRecord> getOneReadCache(long articleId, boolean sevenFlag) {
        String json = redisTemplate.opsForValue().
                get("statistics_article_read_id:" + articleId + "_sevenFlag:" + sevenFlag);

        if (json != null)
            return MyJsonUtil.toBeanList(json, WxReadRecord.class);
        return null;
    }

    private void setOneReadCache(long articleId, boolean sevenFlag, List<WxReadRecord> artReadRecord) {
        redisTemplate.opsForValue().set(
                "statistics_article_read_id:" + articleId + "_sevenFlag:" + sevenFlag,
                MyJsonUtil.toJsonStr(artReadRecord),
                cacheTime
        );
    }

    @Override
    public List<SharePersonWrapper> queryArtSharePerson(long articleId) {
        /* 需要返回：
         * 分享者信息，此分析者所分享的阅读总时长，及其分享的阅读次数
         */
        // 1.查缓存
        String json = redisTemplate.opsForValue().
                get("statistics_article_id:" + articleId + "_sharePerson");
        if (json != null) {
            return MyJsonUtil.toBeanList(json, SharePersonWrapper.class);
        }

        // 2.查数据库
        List<SharePersonWrapper> sharePersonWrappers =
                wxReadRecordMapper.queryArtSharePerson(articleId);
        // 3.rest调用查用友的用户信息
        for (SharePersonWrapper wrapper : sharePersonWrappers) {
            wrapper.setIuapUser(
                    iuapClient.getUserById(wrapper.getShareId()));
        }

        // 4.放缓存
        redisTemplate.opsForValue().set(
                "statistics_article_id:" + articleId + "_sharePerson",
                MyJsonUtil.toJsonStr(sharePersonWrappers),
                cacheTime
        );
        return sharePersonWrappers;
    }

}
