package com.scrm.marketing;

import com.scrm.marketing.entity.*;
import com.scrm.marketing.mapper.*;
import com.scrm.marketing.util.MyDateTimeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class MarketingApplicationTests {

    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private ArtCusReadMapper articleCustomerReadMapper;
    @Resource
    private ProCusBpLogMapper productCustomerBpLogMapper;

    @Test
    void contextLoads() {
        List<Article> articles =
                articleMapper.selectList(null);
        articles.forEach(System.out::println);

        List<ArticleCustomerRead> articleCustomerReads = articleCustomerReadMapper.selectList(null);
        articleCustomerReads.forEach(System.out::println);


        List<ProductCustomerBpLog> productCustomerBpLogs = productCustomerBpLogMapper.selectList(null);
        productCustomerBpLogs.forEach(System.out::println);
    }

    @Resource
    private WxReadRecordMapper wxReadRecordMapper;
    @Test
    void test1() {
        WxReadRecord wxReadRecord=new WxReadRecord();
        wxReadRecord.setArticleId(10L);
        wxReadRecord.setWid(1L);
        wxReadRecord.setOpenid("oSLXk6DwZJ1VcXZQH4aPfk");
        wxReadRecord.setShareId(1L);
        wxReadRecord.setReadDate(MyDateTimeUtil.getNowDate());
        wxReadRecord.setReadTime(100);

        wxReadRecordMapper.insert(wxReadRecord);

        List<WxReadRecord> wxReadRecords = wxReadRecordMapper.selectList(null);
        wxReadRecords.forEach(System.out::println);
    }


}
