package com.scrm.marketing.util.resp;

/**
 * @author fzk
 * @date 2021-11-07 16:15
 * 返回结果的常量池
 */
public class ResultCache extends Result {
    public static ResultCache SUCCESS_CACHE = new ResultCache(200, "ok", null);
    public static ResultCache FAIL_CACHE = new ResultCache(CodeEum.FAIL.getCode(), CodeEum.FAIL.getMsg(), null);
    public static ResultCache PARAM_MISS_CACHE = new ResultCache(CodeEum.PARAM_MISS.getCode(), CodeEum.PARAM_MISS.getMsg(), null);
    public static ResultCache PARAM_ERROR_CACHE = new ResultCache(CodeEum.PARAM_ERROR.getCode(), CodeEum.PARAM_ERROR.getMsg(), null);

    private ResultCache(int code, String msg, Object data) {
        super(code, msg, data);
    }

    @Override
    public void setCode(int code) {
        throw new RuntimeException("常量对象不允许修改");
    }

    @Override
    public void setMsg(String msg) {
        throw new RuntimeException("常量对象不允许修改");
    }

    @Override
    public void setData(Object data) {
        throw new RuntimeException("常量对象不允许修改");
    }

    @Override
    public Result addMsg(String addMsg) {
        throw new RuntimeException("常量对象不允许修改");
    }
}

