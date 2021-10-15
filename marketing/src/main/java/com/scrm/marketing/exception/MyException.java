package com.scrm.marketing.exception;

import com.scrm.marketing.util.resp.CodeEum;

/**
 * @author fzk
 * @date 2021-10-14 19:16
 */
public class MyException extends Exception {
    private int code;

    public MyException(int code,String msg) {
        super(msg);
        this.code=code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
