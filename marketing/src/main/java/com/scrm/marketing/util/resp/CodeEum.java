package com.scrm.marketing.util.resp;

/**
 * @author fzk
 * @date 2021-10-14 19:34
 */
public enum CodeEum {
    SUCCESS(200, "ok"),
    ERROR(1000, "系统错误"),
    AUTH_FAIL(1003, "无权限"),
    NOT_EXIST(1004, "未找到资源"),
    PARAM_MISS(1009, "缺少参数"),
    PARAM_ERROR(1010, "参数错误");

    private int code;
    private String msg;

    private CodeEum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
