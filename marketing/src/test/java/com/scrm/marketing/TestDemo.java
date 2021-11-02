package com.scrm.marketing;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.druid.sql.visitor.functions.Char;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

/**
 * @author fzk
 * @date 2021-10-19 19:37
 */
public class TestDemo {
    @Test
    void test1() {
        Date nowDate=new Date();
        Date lastWeek=DateUtil.lastWeek();
        Date lastMonth=DateUtil.lastMonth();
        System.out.println(DateUtil.endOfDay(lastWeek));
        System.out.println(DateUtil.endOfDay(lastMonth));
        System.out.println(DateUtil.endOfDay(nowDate));
    }

    @Test
    void test2(){
       String str= HtmlUtils.htmlEscape("<meta name=\"description\" content=\"史上最全操作系统教程\" />");
        System.out.println(str);
    }
}
