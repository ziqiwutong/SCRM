package com.scrm.manage.controller;

import com.scrm.manage.service.IuapService;
import com.scrm.manage.share.iuap.UserInfoResult;
import com.scrm.manage.util.resp.CodeEum;
import com.scrm.manage.util.resp.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用友iuap相关的controller
 *
 * @author fzk
 * @date 2021-11-18 20:12
 */
@RestController
@RequestMapping("/iuap")
public class IuapController {

    @Resource
    private IuapService iuapService;

    /**
     * 使用空间的用户作为免登数据依据。比如：空间用户ID、手机号等
     * 请求地址：https://openapi.yonyoucloud.com/certified/userInfo/{{:code}}?access_token=xxx
     *
     * @param code 用户在点击轻应用之后跳转的URL会自动附加code参数，此code由友空间统一生成，以此来作为用户的授权登录key
     * @return result
     */
    @GetMapping("/userInfo")
    public Result getUserInfo(@RequestParam("code") String code) {
        UserInfoResult infoResult = iuapService.getUserInfo(code);

        if (infoResult != null && "0".equals(infoResult.getCode()))
            return Result.success(infoResult.getData());

        return Result.error(CodeEum.FAIL,
                infoResult == null ? "没拿到任何信息" :
                        infoResult.getCode() + ":" + infoResult.getMsg());
    }

    /**
     * 用友JSAPI鉴权
     *
     * @return result
     */
    @GetMapping("/jsApi")
    public Result jsapi() {
        return Result.success(iuapService.jsTicket());
    }
}
