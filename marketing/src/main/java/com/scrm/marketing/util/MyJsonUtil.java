package com.scrm.marketing.util;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * @author fzk
 * @date 2021-09-09 11:18
 * <p>
 * 根据jackson工具进行序列化
 * 根据Hutool工具反序列化
 */
public class MyJsonUtil {
    public static String writeToString(Object o) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(o);
    }

    public static <T> List<T> toBeanArray(String json, Class<T> type) {
        return JSONUtil.parseArray(json).toList(type);
    }

    public static <T> T toBean(String json, Class<T> type) {
        return JSONUtil.parseObj(json).toBean(type);
    }
}
