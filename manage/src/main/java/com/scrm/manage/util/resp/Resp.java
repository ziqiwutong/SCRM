package com.scrm.manage.util.resp;

import java.util.HashMap;
import java.util.Map;

public class Resp {
    public static Resp PARAM_WRONG;
    private int code;
    private String msg;
    private Object data;

    public Resp() {

    }

    private Resp(CodeEum codeEum) {
        this.code = codeEum.getCode();
        this.msg = codeEum.getMsg();
    }

    public static Resp success(CodeEum codeEum) {
            return new Resp(codeEum);
        }

    public static Resp success() {
        return new Resp(CodeEum.SUCCESS);
    }

    public static Resp error(CodeEum codeEum) {
        return new Resp(codeEum);
    }

    public static Resp error() {
        return new Resp(CodeEum.ERROR);
    }

    public static Resp info(CodeEum codeEum) {
        return new Resp(codeEum);
    }

    public boolean respSuccess() {
        return this.code == 0;
    }

    public Resp put(String key, Object value) {
        if (key != null && key.length() != 0) {
            if (this.data == null) {
                this.data = new HashMap<String, Object>();
                ((Map)this.data).put(key, value);
            } else if (this.data instanceof Map) {
                ((Map)this.data).put(key, value);
            }

            return this;
        } else {
            return this;
        }
    }

    public Resp setData(Object data) {
        this.data = data;
        return this;
    }

    public static Resp getParamWrong() {
        return PARAM_WRONG;
    }

    public static void setParamWrong(Resp paramWrong) {
        PARAM_WRONG = paramWrong;
    }

    public String getMsg() {
        return this.msg;
    }

    public Resp setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return this.data;
    }

    public String toString() {
        return "Resp{errorCode=" + this.code + ", msg='" + this.msg + '\'' + ", data=" + this.data + '}';
    }

    static {
        PARAM_WRONG = new Resp(CodeEum.PARAM_ERROR);
    }
}
