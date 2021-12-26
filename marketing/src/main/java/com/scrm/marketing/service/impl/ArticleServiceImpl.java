package com.scrm.marketing.service.impl;

import com.scrm.marketing.entity.Article;
import com.scrm.marketing.exception.MyException;
import com.scrm.marketing.mapper.*;
import com.scrm.marketing.service.ArticleService;
import com.scrm.marketing.share.iuap.IuapClient;
import com.scrm.marketing.share.iuap.IuapUser;
import com.scrm.marketing.util.MyAssert;
import com.scrm.marketing.util.MyJsonUtil;
import com.scrm.marketing.util.resp.CodeEum;
import com.scrm.marketing.util.resp.PageResult;
import com.scrm.marketing.util.resp.Result;
import io.netty.util.internal.ThreadLocalRandom;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.*;

/**
 * @author fzk
 * @date 2021-10-14 20:24
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private WxReadRecordMapper wxReadRecordMapper;
    @Resource
    private IuapClient iuapClient;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    private static final String articleKeyPrefix = "article:";
    private static final Duration cacheTime = Duration.ofMinutes(20L);

    @Override
    public Result getArticleDetail(Long id, String shareId) {
        // 1. 从缓存查
        Article article;
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();

        String article_json = opsForValue.get(articleKeyPrefix + id);
        if (article_json != null)
            article = MyJsonUtil.toBean(article_json, Article.class);

        else {
            // 2.缓存没拿到，先互斥锁定
            Boolean lockFlag = Boolean.FALSE;
            for (int i = 0; i < 3; i++) {
                lockFlag = opsForValue.setIfAbsent(
                        articleKeyPrefix + id + "_lock",
                        Thread.currentThread().getName());
                if (Boolean.TRUE.equals(lockFlag)) break;
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextLong(0, 500));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 3.拿到锁了或循环了3次了，再从缓存拿
            article_json = opsForValue.get(articleKeyPrefix + id);
            if (article_json != null) {
                article = MyJsonUtil.toBean(article_json, Article.class);
            } else {
                // 4.确实没有，再去数据库查询
                article = articleMapper.selectById(id);
                // 放入缓存
                opsForValue.set(articleKeyPrefix + id, MyJsonUtil.toJsonStr(article), cacheTime);
            }
            // 5.释放锁
            if (Boolean.TRUE.equals(lockFlag))
                redisTemplate.delete(articleKeyPrefix + id + "_lock");
        }
        if (article == null)
            return Result.error(CodeEum.NOT_EXIST, "文章id: " + id + " 不存在");

        // 6.将productIdsJson 转为 productIds属性
        Article.productIdsHandle(article);

        // 7.1 shareId为空
        if (shareId == null) {
            return Result.success(article);
        }
        // 7.2、shareId不为空
        else {
            IuapUser shareUser = iuapClient.getUserById(shareId);

            if (shareUser == null)
                return Result.error(CodeEum.NOT_EXIST).addMsg("分享者id不存在");

            Map<String, Object> map = new TreeMap<>();
            map.put("article", article);
            map.put("user", shareUser);
            return Result.success(map);
        }
    }

    @Override
    public PageResult queryPage(int pageNum, int pageSize, Integer examineFlag, Integer materialType) {
        int index = (pageNum - 1) * pageSize;
        int total = articleMapper.queryCount(index, pageSize, examineFlag, materialType);
        List<Article> articles = articleMapper.queryPage(index, pageSize, examineFlag, materialType);
        // 对productIds处理
        for (Article article : articles) {
            Article.productIdsHandle(article);
        }
        return PageResult.success(articles, total, pageNum);
    }

    @Override
    public void insert(Article article, String loginId) {
        // 1、获取用户信息
        IuapUser user = iuapClient.getUserById(loginId);
        if (user == null)
            throw new MyException(CodeEum.CODE_PARAM_ERROR, "loginId: " + loginId + " not exists");
        // 2、完善article属性
        article.setAuthorId(loginId);//作者id
        article.setAuthorName(user.getName());//作者名称

        article.setArticleViewTimes(0);
        article.setArticleReadTimeSum(0L);

        // productIds 转为 productIdsJson
        if (article.getProductIds() == null)
            article.setProductIdsJson("[]");
        else article.setProductIdsJson(MyJsonUtil.toJsonStr(article.getProductIds()));

        /*
         * 目前暂时先全部审核通过
         */
        article.setExamineFlag(Article.EXAMINE_FLAG_ACCESS);//审核标记：待审核
        System.out.println("========warning：目前对于文章的插入处理，全部选择审核通过=======");

        if (article.getMaterialType() == null)
            article.setMaterialType(Article.MATERIAL_TYPE_PERSONAL);//默认是个人素材

        // 对于createTime和updateTime设为null
        article.setCreateTime(null);
        article.setUpdateTime(null);

        // 3、插入
        articleMapper.insert(article);
    }

    @Override
    public void update(Article article, String loginId) {
        MyAssert.notNull("article can not be null and loginId", article, loginId);
        /*
         * 对于createTime和updateTime设为null
         */
        article.setCreateTime(null);
        article.setUpdateTime(null);
        // 将productIds 转为 productIdsJson属性
        if (article.getProductIds() != null)
            article.setProductIdsJson(MyJsonUtil.toJsonStr(article.getProductIds()));

        articleMapper.updateById(article);

        redisTemplate.delete(articleKeyPrefix + article.getId());// 刷新缓存
    }

    @Override
    @Transactional//开启事务
    public void delete(Long id) throws MyException {
        /*
         删除文章，应该需要级联删除
         1.微信用户阅读记录：mk_wx_read_record
           */
        // 1.删除文章的微信阅读记录
        wxReadRecordMapper.deleteByAid(id);

        // 2、删除文章
        if (articleMapper.deleteById(id) != 1)
            throw new MyException(CodeEum.CODE_ERROR, "删除失败,可能是文章已经被删除");
        // 3、删除缓存
        redisTemplate.delete(articleKeyPrefix + id);
    }

    @Override
    public void examine(Long id, String loginId, Integer examineFlag, String examineNotes) {
        // 1、查出审核人
        IuapUser user = iuapClient.getUserById(loginId);
        if (user == null) throw new MyException(CodeEum.CODE_PARAM_ERROR, "审核人id不存在");
        String examineName = user.getName();
        // 2.执行修改操作
        articleMapper.examine(id, loginId, examineName, examineFlag, examineNotes);

        // 3.删除缓存
        redisTemplate.delete(articleKeyPrefix + id);
    }

    /**
     * 默认根据标题模糊查询审核通过的文章
     *
     * @param title 标题
     * @return list
     */
    @Override
    public List<Article> queryByTitle(String title) {
        MyAssert.notNull("title can not be null", title);
        // 查询通过审核的文章
        List<Article> articles = articleMapper.queryByTitle(title, Article.EXAMINE_FLAG_ACCESS);
        // productIds 处理
        for (Article article : articles) {
            Article.productIdsHandle(article);
        }
        return articles;
    }
}
