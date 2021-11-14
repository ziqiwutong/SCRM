package com.scrm.marketing.util;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fzk
 * @date 2021-09-09 11:18
 * <p>
 * 根据jackson工具进行序列化
 * 根据Hutool工具反序列化
 */
public class MyJsonUtil {
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    public static String writeToString(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    public static <T> List<T> toBeanArray(String json, Class<T> type) {
        return JSONUtil.parseArray(json).toList(type);
    }

    public static <T> T toBean(String json, Class<T> type) {
        return JSONUtil.parseObj(json).toBean(type);
    }

    /*这个和上面那个的区别在于：[1,,3]情况下，这个会报错，上面那个toBeanArray用null代替*/
    public static <T> List<T> toArray(String json, Class<T> type) throws JsonProcessingException {
        List list = objectMapper.readValue(json, List.class);
        List<T> resultList = new ArrayList<>(list.size());
        for (Object o : list) {
            resultList.add((T) o);
        }
        return resultList;
    }
}
