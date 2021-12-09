package com.scrm.service;

import com.scrm.service.dao.CustomerDao;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author fzk
 * @date 2021-12-09 17:21
 */
@SpringBootTest
public class AppTest {
    @Resource
    private CustomerDao customerDao;

    @Test
    void test1() {
        System.out.println("出现这个说明maven没有设置跳过test模块");
    }
}
