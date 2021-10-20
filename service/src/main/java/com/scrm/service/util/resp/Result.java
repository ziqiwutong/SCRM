package com.scrm.service.util.resp;

/**
 * @author fzk
 * @date 2021-10-20 15:52
 */
public class Result {
    private int code;
    private String msg;
    private Object data;

    public Result() {
    }


    public Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result success() {
        return init(CodeEum.SUCCESS, null);
    }

    public static Result success(Object data) {
        return init(CodeEum.CODE_SUCCESS, "ok", data);
    }

    public static Result error(CodeEum codeEum) {
        return init(codeEum.getCode(), codeEum.getMsg(), null);
    }

    public static Result error(CodeEum codeEum, Object data) {
        return init(codeEum.getCode(), codeEum.getMsg(), data);
    }

    public static Result init(int code, String msg, Object data) {
        return new Result(code, msg, data);
    }

    public static Result init(CodeEum codeEum, Object data) {
        return new Result(codeEum.getCode(), codeEum.getMsg(), data);
    }

    /*getter和setter方法*/

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}

