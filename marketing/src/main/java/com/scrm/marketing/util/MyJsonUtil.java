package com.scrm.marketing.util;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * <p>
 * 完全选择fastjson进行序列化与反序列化
 * <p>
 * 可能会抛出JSONException, 由于这是RuntimeException的子类, 所以不会让强制处理，
 * 而且按照异常原则，从一开始就要改避免出现这些异常，而不是想着如何去捕获异常以及处理异常
 *
 * @author fzk
 * @date 2021-09-09 11:18
 */
public class MyJsonUtil {
    public static String toJsonStr(Object o) {
        return JSON.toJSONString(o);
    }

    public static <T> List<T> toBeanList(String json, Class<T> type) {
        return JSON.parseArray(json, type);
    }

    public static <T> T toBean(String json, Class<T> type) {
        return JSON.parseObject(json, type);
    }
}
