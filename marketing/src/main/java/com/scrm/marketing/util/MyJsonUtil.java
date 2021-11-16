package com.scrm.marketing.util;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * @author fzk
 * @date 2021-09-09 11:18
 * <p>
 * 根据jackson工具进行序列化
 * 根据Hutool工具反序列化
 */
public class MyJsonUtil {
    public static String toJsonStr(Object o) {
        return JSON.toJSONString(o);
    }

    public static <T> List<T> toBeanList(String json, Class<T> type) {
        return JSON.parseArray(json,type);
    }

    public static <T> T toBean(String json, Class<T> type) {
        return JSON.parseObject(json,type);
    }
}
