package com.scrm.marketing.service.impl;

import cn.hutool.core.date.DateUtil;
import com.scrm.marketing.entity.Article;
import com.scrm.marketing.entity.ArticleCustomerRead;
import com.scrm.marketing.entity.ArticleShareRecord;
import com.scrm.marketing.entity.User;
import com.scrm.marketing.exception.MyException;
import com.scrm.marketing.feign.UserClient;
import com.scrm.marketing.mapper.ArtCusReadMapper;
import com.scrm.marketing.mapper.ArticleMapper;
import com.scrm.marketing.mapper.ArticleShareRecordMapper;
import com.scrm.marketing.mapper.UserMapper;
import com.scrm.marketing.service.ArticleService;
import com.scrm.marketing.util.MyAssert;
import com.scrm.marketing.util.resp.CodeEum;
import com.scrm.marketing.util.resp.PageResult;
import com.scrm.marketing.util.resp.Result;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fzk
 * @date 2021-10-14 20:24
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private UserClient userClient;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ArticleShareRecordMapper articleShareRecordMapper;
    @Resource
    private ArtCusReadMapper articleCustomerReadMapper;

    @Override
    @Transactional//开启事务
    public Result getArticleDetail(Long id, @Nullable Long shareId) throws MyException {
        Article article = articleMapper.selectById(id);
        if (article == null)
            return Result.error(CodeEum.NOT_EXIST);
        // 1、shareId为空
        if (shareId == null) {
            return Result.success(article);
        }
        // 2、shareId不为空
        else {
            // 2.1 先查出user
            User shareUser = userMapper.selectById(shareId);
            if (shareUser == null)
                return Result.error(CodeEum.PARAM_ERROR);
            // 2.2 再根据shareId和articleId查文章分享分享
            List<ArticleShareRecord> articleShareRecords =
                    articleShareRecordMapper.selectByAIdAndSid(id, shareId);

            ArticleShareRecord articleShareRecord = null;
            // 2.3 没有记录，则新增
            if (articleShareRecords.isEmpty()) {
                articleShareRecord = new ArticleShareRecord();
                articleShareRecord.setArticleId(id);//文章id
                articleShareRecord.setShareId(shareId);// 分享者id
                articleShareRecord.setShowShareFlag(true);//默认展示分享标记
                articleShareRecord.setReadRecord(null);
                if (1 != articleShareRecordMapper.insert(articleShareRecord))
                    throw new MyException(CodeEum.ERROR.getCode(), "分享记录添加失败");

            }
            // 2.4 有1条记录
            else if (articleShareRecords.size() == 1) {
                articleShareRecord = articleShareRecords.get(0);
            }
            // 2.5 多条记录，抛异常
            else
                throw new MyException(CodeEum.ERROR.getCode(), "分享记录表存在多条相关记录");

            Map<String, Object> map = new HashMap<>();
            map.put("article", article);
            map.put("user", shareUser);
            map.put("showShareFlag", articleShareRecord.getShowShareFlag());
            return Result.success(map);
        }

    }

    @Override
    public PageResult queryPage(int pageNum, int pageSize, Integer examineFlag) {
        int index = (pageNum - 1) * pageSize;
        int total = articleMapper.queryCount(index, pageSize, examineFlag);
        List<Article> articles = articleMapper.queryPage(index, pageSize, examineFlag);
        return PageResult.success(articles, total, pageNum);
    }

    @Override
    public void insert(Article article, @NonNull Long loginId) {
        // 1、获取用户信息
        // 1.1 从Redis查

        // 1.2 RPC调用
//        Result rpcResult = userClient.getUser(loginId);
//        if(rpcResult.getCode()!=200)
//            throw new MyException(rpcResult.getCode(),rpcResult.getMsg());
//
//        Map userMap=(Map)rpcResult.getData();
//        String username = userMap.get("username").toString();

        User user = userMapper.selectById(loginId);
        if (user == null)
            throw new MyException(CodeEum.CODE_PARAM_ERROR, "loginId" + loginId + "not exists");
        // 2、完善article属性
        article.setAuthorId(loginId);//作者id
        article.setAuthorName(user.getUsername());//作者名称

        article.setArticleViewTimes(0);
        article.setArticleReadTimeSum(0L);

        /**
         * 目前暂时先全部审核通过
         */
        article.setExamineFlag(Article.EXAMINE_FLAG_ACCESS);//审核标记：待审核


        // 3、插入
        if (articleMapper.insert(article) != 1)
            throw new MyException(CodeEum.ERROR.getCode(), "文章添加失败");

    }

    @Override
    public void update(@NonNull Article article, Long loginId) throws MyException {
        if (articleMapper.updateById(article) != 1)
            throw new MyException(CodeEum.ERROR.getCode(), "文章不存在");
    }

    @Override
    @Transactional//开启事务
    public void delete(@Nullable Long id) throws MyException {
        /*
         删除文章，应该需要级联删除
         1.article_share_record
         2.article_customer_read
         再删除本身
         */
        // 1.删除文章分享记录
        articleShareRecordMapper.deleteByArticleId(id);
        // 2.删除文章客户阅读记录
        articleCustomerReadMapper.deleteByArticleId(id);
        // 3、删除文章
        if (articleMapper.deleteById(id) != 1)
            throw new MyException(CodeEum.CODE_ERROR, "删除失败，可能是文章不存在");
    }

    @Override
    @Transactional//开启事务
    public void examine(@NonNull Long id, @NonNull Long loginId, @NonNull Integer examineFlag, String examineNotes) throws MyException {
        // 1、查出审核人
        String examineName = userMapper.selectById(loginId).getUsername();

        // 2.执行修改操作
        if (articleMapper.examine(id, loginId, examineName, examineFlag, examineNotes) != 1)
            throw new MyException(CodeEum.CODE_ERROR, "审核失败");
    }

    @Override
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
        // 情况1：无文章id，则分页查询文章总阅读时长，从 mk_article 表查
        else {
            // 计算偏移量
            int offset = (pageNum - 1) * pageSize;
            // 从mk_article表查询文章总阅读时长
            List<Article> articles = articleMapper.queryPage(offset, pageSize, 1);
            // 查询文章总数
            int total = articleMapper.queryCount(offset, pageSize, 1);
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
        return articleMapper.queryByTitle(title, Article.EXAMINE_FLAG_ACCESS);
    }
}
