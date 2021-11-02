package com.scrm.marketing;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.scrm.marketing.entity.*;
import com.scrm.marketing.feign.TestClient;
import com.scrm.marketing.mapper.*;
import com.scrm.marketing.satoken.SaTokenJwtUtil;
import com.scrm.marketing.util.resp.Result;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class MarketingApplicationTests {

    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private ArtCusReadMapper articleCustomerReadMapper;
    @Resource
    private ArticleShareRecordMapper articleShareRecordMapper;
    @Resource
    private CustomerReadLogMapper readLogMapper;
    @Resource
    private ProCusBpLogMapper productCustomerBpLogMapper;

    @Test
    void contextLoads() {
        List<Article> articles =
                articleMapper.selectList(null);
        articles.forEach(System.out::println);

        List<ArticleCustomerRead> articleCustomerReads = articleCustomerReadMapper.selectList(null);
        articleCustomerReads.forEach(System.out::println);

        List<ArticleShareRecord> articleShareRecords = articleShareRecordMapper.selectList(null);
        articleShareRecords.forEach(System.out::println);

        List<CustomerReadLog> customerReadLogs = readLogMapper.selectList(null);
        customerReadLogs.forEach(System.out::println);

        List<ProductCustomerBpLog> productCustomerBpLogs = productCustomerBpLogMapper.selectList(null);
        productCustomerBpLogs.forEach(System.out::println);
    }

    @Test
    void saTokenTest(){
        StpUtil.login(1);
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        System.out.println(tokenInfo.getTokenName());
        System.out.println(tokenInfo.getTokenValue());

        String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYmYiOjE2MzQzMDEwNjYsImlzcyI6ImF1dGgwIiwiZXhwIjoxNjM0OTA1ODY2LCJ1c2VySUQiOiIxIiwiaWF0IjoxNjM0MzAxMDY2fQ.4R5yZGXuduwYegEuuGvAt5EpB9_bePhpwVS7toMkNqo";
        String loginId = SaTokenJwtUtil.getLoginId(token);
        System.out.println(loginId);
    }

    @Resource
    private TestClient testClient;
    @Test
    void feignTest(){
        Map<String,Object> map=new HashMap<>();
//        map.put("Authorization","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySUQiOiJzY3JtMTIzIiwiaWF0IjoxNjM0NjQ2OTEwLCJleHAiOjE2MzUyNTE3MTB9.C3Tp_mN2u_K5erHX3LZj_hp5rR2hr4iUHR_ieZqCY7k");
        String result = testClient.getArticleDetail(map);
//        System.out.println(result);
        result = result.replaceAll("\\s", "");
        System.out.println(result);
    }

}
