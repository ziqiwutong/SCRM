package com.scrm.marketing.exception;

import com.scrm.marketing.util.resp.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author fzk
 * @date 2021-10-14 19:14
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 自定义的异常的处理
     *
     * @param e 自定义的异常
     * @return 返回Resp
     */
    @ExceptionHandler({Exception.class})
    public Result handle(Exception e) {
        if (e instanceof MyException) {
            MyException me = (MyException) e;
            return Result.init(me.getCode(), me.getMessage(), null);
        } else {
            return Result.init(1000, "异常类型：" + e.getClass() + "；异常详情：" + e.getMessage(), null);
        }
    }

}
