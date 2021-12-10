package com.scrm.marketing.service.impl;

import cn.hutool.core.date.DateUtil;
import com.scrm.marketing.entity.Article;
import com.scrm.marketing.entity.ArticleCustomerRead;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
    private ArtCusReadMapper articleCustomerReadMapper;
    @Resource
    private WxReadRecordMapper wxReadRecordMapper;
    @Resource
    private IuapClient iuapClient;

    @Override
    public Result getArticleDetail(Long id, String shareId) {
        // 0. 查询文章所有
        // 0.1 从缓存查
        Article article;
//        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
//        String article_json = opsForValue.get("article:id:1");
//        article = MyJsonUtil.toBean(article_json, Article.class);


        article = articleMapper.selectById(id);
        if (article == null)
            return Result.error(CodeEum.NOT_EXIST);

        // 0.1 将productIdsJson 转为 productIds属性
        Article.productIdsHandle(article);

        // 1、shareId为空
        if (shareId == null) {
            return Result.success(article);
        }
        // 2、shareId不为空
        else {
            // 2.1 先查出user
            IuapUser shareUser = iuapClient.getUserById(shareId);

            if (shareUser == null)
                return Result.error(CodeEum.NOT_EXIST).addMsg("分享者id不存在");

            Map<String, Object> map = new HashMap<>(2, 1.0f);
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
            throw new MyException(CodeEum.CODE_PARAM_ERROR, "loginId" + loginId + "not exists");
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
    }

    @Override
    @Transactional//开启事务
    public void delete(Long id) throws MyException {
        /*
         删除文章，应该需要级联删除
             // 1.article_share_record
             // 2.article_customer_read
             3.微信用户阅读记录：mk_wx_read_record
         注意：此处保留了相关文章的客户阅读记录
         */
        // 1.删除文章分享记录
        // articleShareRecordMapper.deleteByArticleId(id);
        // 2.不删除文章客户阅读记录
        //articleCustomerReadMapper.deleteByArticleId(id);

        // 3.删除文章的微信阅读记录
        wxReadRecordMapper.deleteByAid(id);

        // 4、删除文章
        if (articleMapper.deleteById(id) != 1)
            throw new MyException(CodeEum.CODE_ERROR, "删除失败,可能是文章已经被删除");
    }

    @Override
    public void examine(Long id, String loginId, Integer examineFlag, String examineNotes) {
        // 1、查出审核人
        IuapUser user = iuapClient.getUserById(loginId);
        if (user == null) throw new MyException(CodeEum.CODE_PARAM_ERROR, "审核人id不存在");
        String examineName = user.getName();
        // 2.执行修改操作
        articleMapper.examine(id, loginId, examineName, examineFlag, examineNotes);
    }

    @Override
    /*感觉这里应该不需要对productIds属性进行处理*/
    public Result queryArticleRead(Long articleId, Boolean sevenFlag, Integer pageNum, Integer pageSize) {
        // 情况2：有文章id，则查询特定文章的7天或者30天的阅读时长，从 mk_article_customer_read 表查
        if (articleId != null && sevenFlag != null) {
            // 这里根据sevenFlag生成查询起始时间
            // 注意：虽然read_date的比较应该以日期比较，但是这里用日期时间却工作得很好哦
            Date startDate;
            if (sevenFlag) startDate = DateUtil.lastWeek();
                // 查询30天
            else startDate = DateUtil.lastMonth();

            // 从mk_article_customer_read表查
            List<ArticleCustomerRead> articleCustomerReads =
                    articleCustomerReadMapper.queryArticleRead(articleId, startDate);
            return Result.success(articleCustomerReads);
        }
        // 情况1：无文章id，则分页查询文章总阅读时长，从 mk_article 表查：审核通过，无论哪种materialType
        else {
            // 计算偏移量
            int offset = (pageNum - 1) * pageSize;
            // 从mk_article表查询文章总阅读时长
            List<Article> articles = articleMapper.queryPage(offset, pageSize, Article.EXAMINE_FLAG_ACCESS, null);
            // 查询文章总数
            int total = articleMapper.queryCount(offset, pageSize, Article.EXAMINE_FLAG_ACCESS, null);
            return PageResult.success(articles, total, pageNum);
        }
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
