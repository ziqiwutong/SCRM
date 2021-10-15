package com.scrm.marketing.service.impl;

import com.scrm.marketing.entity.Article;
import com.scrm.marketing.entity.User;
import com.scrm.marketing.exception.MyException;
import com.scrm.marketing.feign.UserClient;
import com.scrm.marketing.mapper.ArticleCustomerReadMapper;
import com.scrm.marketing.mapper.ArticleMapper;
import com.scrm.marketing.mapper.ArticleShareRecordMapper;
import com.scrm.marketing.mapper.UserMapper;
import com.scrm.marketing.service.ArticleService;
import com.scrm.marketing.util.MyConstant;
import com.scrm.marketing.util.resp.CodeEum;
import com.scrm.marketing.util.resp.PageResult;
import com.scrm.marketing.util.resp.Result;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.sql.Wrapper;
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
    private ArticleCustomerReadMapper articleCustomerReadMapper;

    @Override
    public Result getArticleDetail(Long id) {
        Article article = articleMapper.selectById(id);
        if (article == null)
            return Result.error(CodeEum.NOT_EXIST);
        else return Result.success(article);
    }

    @Override
    public PageResult queryPage(int pageNum, int pageSize, Integer examineFlag) {
        int index = (pageNum - 1) * pageSize;
        int total = articleMapper.queryCount(index, pageSize, examineFlag);
        List<Article> articles = articleMapper.queryPage(index, pageSize, examineFlag);
        return PageResult.success(articles, total, pageNum);
    }

    @Override
    @Transactional
    public void insert(Article article, @NonNull Long loginId) throws MyException {
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
        // 2、完善article属性
        article.setAuthorId(loginId);//作者id
        article.setAuthorName(user.getUsername());//作者名称

        article.setArticleViewTimes(0);
        article.setArticleReadTimeSum(0L);

        article.setExamineFlag(MyConstant.EXAMINE_WAIT);//审核标记：待审核

        // 3、插入
        if (articleMapper.insert(article) != 1)
            throw new MyException(CodeEum.ERROR.getCode(), "文章添加失败");

    }

    @Override
    @Transactional//开启事务
    public void update(@NonNull Article article, Long loginId) throws MyException {
        if (articleMapper.updateById(article) != 1)
            throw new MyException(CodeEum.ERROR.getCode(), "修改失败");
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
}
