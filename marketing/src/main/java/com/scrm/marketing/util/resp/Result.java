package com.scrm.marketing.util.resp;


/**
 * @author fzk
 * @date 2021-10-14 19:29
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

    public Result(CodeEum codeEum, Object data) {
        this.code = codeEum.getCode();
        this.msg = codeEum.getMsg();
        this.data = data;
    }

    public static Result success() {
        return new Result(CodeEum.SUCCESS, null);
    }

    public static Result success(Object data) {
        return new Result(CodeEum.SUCCESS, data);
    }

    public static Result error(CodeEum codeEum) {
        return new Result(codeEum, null);
    }

    public static Result error(CodeEum codeEum, Object data) {
        return new Result(codeEum, data);
    }

    public static Result init(int code, String msg, Object data) {
        return new Result(code, msg, data);
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
