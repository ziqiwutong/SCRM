package com.scrm.marketing.exception;

import cn.dev33.satoken.exception.*;
import com.scrm.marketing.util.resp.CodeEum;
import com.scrm.marketing.util.resp.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.cn;

/**
 * @author fzk
 * @date 2021-10-14 19:14
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 自定义的异常的处理
     *
     * @param me 自定义的异常
     * @return 返回Result
     */
    @ExceptionHandler({MyException.class})
    public Result handle(MyException me) {
        return Result.init(me.getCode(), me.getMessage(), null);
    }

    /**
     * 拦截来自Sa-token抛出的异常
     *
     * @param e        SaTokenException
     * @param request  请求
     * @param response 响应
     * @return 返回Result
     */
    @ExceptionHandler({SaTokenException.class})
    public Result handlerException(SaTokenException e, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // 打印堆栈，以供调试，上线之前需要注释掉
        System.out.println("全局异常，上线之前需要注释掉打印信息---------------");
        e.printStackTrace();

        // 不同异常返回不同状态码
        Result result = null;
        if (e instanceof NotLoginException) {    // 如果是未登录异常
            NotLoginException ee = (NotLoginException) e;
            result = Result.error(CodeEum.NOT_LOGIN);
            result.setMsg(ee.getMessage());
        } else if (e instanceof NotRoleException) {        // 如果是角色异常
            NotRoleException ee = (NotRoleException) e;
            result = Result.error(CodeEum.AUTH_FAIL);
            result.setMsg("无此角色：" + ee.getRole());
        } else if (e instanceof NotPermissionException) {    // 如果是权限异常
            NotPermissionException ee = (NotPermissionException) e;
            result = Result.error(CodeEum.AUTH_FAIL);
            result.setMsg("无此权限：" + ee.getCode());//权限码
        } else if (e instanceof DisableLoginException) {    // 如果是被封禁异常
            DisableLoginException ee = (DisableLoginException) e;
            result = Result.error(CodeEum.AUTH_FAIL);
            result.setMsg("账号被封禁：" + ee.getDisableTime() + "秒后解封");
        } else {    // 普通异常, 输出：500 + 异常信息
            result = Result.error(CodeEum.ERROR);
            result.setMsg(e.getMessage());
        }

        // 返回给前端
        return result;
    }
}
