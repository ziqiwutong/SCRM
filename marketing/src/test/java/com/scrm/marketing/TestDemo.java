package com.scrm.marketing;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.scrm.marketing.share.iuap.AccessToken;
import com.scrm.marketing.util.MyDigestUtil;
import com.scrm.marketing.util.MyJsonUtil;
import com.scrm.marketing.util.MyRandomUtil;
import com.scrm.marketing.share.wx.WxUserInfoResult;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.HtmlUtils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author fzk
 * @date 2021-10-19 19:37
 */
public class TestDemo {
    @Test
    void test1() {
        Date nowDate = new Date();
        Date lastWeek = DateUtil.lastWeek();
        Date lastMonth = DateUtil.lastMonth();
        System.out.println(DateUtil.endOfDay(lastWeek));
        System.out.println(DateUtil.endOfDay(lastMonth));
        System.out.println(DateUtil.endOfDay(nowDate));
    }

    @Test
    void test2() {
        String str = HtmlUtils.htmlEscape("<meta name=\"description\" content=\"史上最全操作系统教程\" />");
        System.out.println(str);
    }

    @Test
    void test3() throws JsonProcessingException {
        String arrayJson="[{\"openid\":\"oSLXk6DwZJ1VcXZQH4aPfk\",\"nickname\":\"南阁子123\",\"sex\":\"0\",\"province\":\"\",\"city\":\"\",\"country\":\"\",\"headimgurl\":\"https://thirdwx.qlogo.cn/mmopen/vi_32/XK9TOvt9x3OQF7tPHwtxlC4BrAMicfRAV6fibz29N5FDQYZtDqa0Ndbhy2WuCqp4YPC383nqyRjn9V05GSOn3q9A/132\",\"unionid\":null,\"errcode\":12,\"errmsg\":null,\"readTime\":89,\"readDate\":\"2021-11-13\",\"readerStatus\":null,\"articleId\":10,\"shareId\":1},{\"openid\":\"oSLXk6DwDDX455ZJ1VcXZQH4aPfk\",\"nickname\":\"南阁子\",\"sex\":\"0\",\"province\":\"\",\"city\":\"\",\"country\":\"\",\"headimgurl\":\"https://thirdwx.qlogo.cn/mmopen/vi_32/XK9TOvt9x3OQF7tPHwtxlC4BrAMicfRAV6fibz29N5FDQYZtDqa0Ndbhy2WuCqp4YPC383nqyRjn9V05GSOn3q9A/132\",\"unionid\":null,\"errcode\":null,\"errmsg\":null,\"readTime\":89,\"readDate\":\"2021-11-13\",\"readerStatus\":null,\"articleId\":10,\"shareId\":1},{\"openid\":\"oSLXk6DwDDX455ZJ1VcXZQH4aPfk\",\"nickname\":\"南阁子\",\"sex\":\"0\",\"province\":\"\",\"city\":\"\",\"country\":\"\",\"headimgurl\":\"https://thirdwx.qlogo.cn/mmopen/vi_32/XK9TOvt9x3OQF7tPHwtxlC4BrAMicfRAV6fibz29N5FDQYZtDqa0Ndbhy2WuCqp4YPC383nqyRjn9V05GSOn3q9A/132\",\"unionid\":null,\"errcode\":null,\"errmsg\":null,\"readTime\":89,\"readDate\":\"2021-11-13\",\"readerStatus\":null,\"articleId\":10,\"shareId\":1}]";
        String json = "{\"openid\":\"oSLXk6DwZJ1VcXZQH4aPfk\",\"nickname\":\"南阁子123\",\"sex\":\"0\",\"province\":\"\",\"city\":\"\",\"country\":\"\",\"headimgurl\":\"https://thirdwx.qlogo.cn/mmopen/vi_32/XK9TOvt9x3OQF7tPHwtxlC4BrAMicfRAV6fibz29N5FDQYZtDqa0Ndbhy2WuCqp4YPC383nqyRjn9V05GSOn3q9A/132\",\"unionid\":null,\"errcode\":null,\"errmsg\":null,\"readTime\":89,\"readDate\":\"2021-11-13\",\"readerStatus\":null,\"articleId\":10,\"shareId\":1}";
        WxUserInfoResult userInfoResult = MyJsonUtil.toBean(json, WxUserInfoResult.class);
        String s = MyJsonUtil.toJsonStr(userInfoResult);
        System.out.println(s);

//        List<WxUserInfoResult> wxUserInfoResults = MyJsonUtil.toBeanList(arrayJson, WxUserInfoResult.class);
//        wxUserInfoResults.forEach(System.out::println);
    }

    @Test
    void test4(){
        String s = RandomUtil.randomStringUpper(16);
        System.out.println(s);
        String s1 = MyRandomUtil.randomStr(16);
        System.out.println(s1);
        System.out.println(s1.length());
        for(int i=0;i<5;i++){
            System.out.println(MyRandomUtil.randomStr(16));
        }
    }

    @Test
    void test5() throws NoSuchAlgorithmException, InvalidKeyException {
        String s= MyDigestUtil.sha256_mac("hello","1");
        System.out.println(s);
        System.out.println(s+null);
    }
}
