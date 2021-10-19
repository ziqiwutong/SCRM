package com.scrm.marketing.util;
/**
 * @author fzk
 * @date 2021-07-12 22:24
 */

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 单例模式返回接口相应数据
 */
@Data
public class ResponseResult implements Serializable {

    private static Map<String, Object> resultData(Integer code, String msg, Object data) {
        Map<String, Object> res = new HashMap<>();
        res.put("code", code);
        res.put("msg", msg);
        res.put("data", data);
        return res;
    }

    /**
     * 成功
     */
    public static Map<String, Object> success() {
        return resultData(200, "操作成功", null);
    }

    /**
     * 成功
     */
    public static Map<String, Object> success(String msg) {
        return resultData(200, msg, null);
    }

    /**
     * 成功
     */
    public static Map<String, Object> success(Object data) {
        return resultData(200, "操作成功", data);
    }

    /**
     * 成功
     */
    public static Map<String, Object> success(String msg, Object data) {
        return resultData(200, msg, data);
    }

    /**
     * 失败
     */
    public static Map<String, Object> error() {
        return resultData(500, "操作失败", null);
    }

    /**
     * 失败
     */
    public static Map<String, Object> error(Integer code) {
        return resultData(code, null, null);
    }

    /**
     * 失败
     */
    public static Map<String, Object> error(Integer code, String msg) {
        return resultData(code, msg, null);
    }

    /**
     * 失败
     */
    public static Map<String, Object> error(String msg) {
        return resultData(500, msg, null);
    }

    /**
     * 失败
     */
    public static Map<String, Object> error(Object data) {
        return resultData(500, "操作失败", data);
    }

    /**
     * 失败
     */
    public static Map<String, Object> error(Integer code, String msg, Object data) {
        return resultData(code, msg, data);
    }

    /**
     * 失败
     */
//    public static Map<String,Object> error(BaseEnums enums) {
//        return resultData(enums.getCode(), enums.getMsg(), null);
//    }
}
