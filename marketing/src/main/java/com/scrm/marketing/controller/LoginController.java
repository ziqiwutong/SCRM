package com.scrm.marketing.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.scrm.marketing.util.resp.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author fzk
 * @date 2021-11-07 17:13
 */
@Controller
public class LoginController {
    @RequestMapping("/mk/login")
    @ResponseBody
    public Result login(@RequestParam("id") Long id) {
        if (id == null) return Result.PARAM_MISS();
        StpUtil.login(id);
        return Result.success(StpUtil.getTokenValue());
    }

    @RequestMapping("/sockjs.html")
    public String sockjs(){
        return "sockjs";
    }
}
