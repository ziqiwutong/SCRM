package com.scrm.marketing;

import com.scrm.marketing.entity.*;
import com.scrm.marketing.mapper.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class MarketingApplicationTests {

    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private ArticleCustomerReadMapper articleCustomerReadMapper;
    @Resource
    private ArticleShareRecordMapper articleShareRecordMapper;
    @Resource
    private CustomerReadLogMapper readLogMapper;
    @Resource
    private ProductCustomerBpLogMapper productCustomerBpLogMapper;

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

}
