package com.scrm.marketing.controller;

import com.scrm.marketing.util.resp.Result;
import com.scrm.marketing.wx.AccessTokenResult;
import com.scrm.marketing.wx.UserInfoResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author fzk
 * @date 2021-11-07 19:28
 */
@RestController
@RequestMapping("/mk/wx")
public class WxController {
    private static final String appId = "wx45421563894fc3f3";
    private static final String appSecret = "fb33e5dca7e5d506da38936fd99414a7";

    //    private static final String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    private static final String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
    //    private static final String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    private static final String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo";
    @Resource
    private RestTemplate restTemplate;

    @RequestMapping("/signature")
    public String signature(
            @RequestParam("signature") String signature,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce,
            @RequestParam("echostr") String echostr
    ) {

        System.out.println(signature);
        System.out.println(timestamp);
        System.out.println(nonce);
        System.out.println(echostr);

        return echostr;
    }


    @GetMapping("/getCode")
    public Result getCode(
            @RequestParam("code") String code,
            @RequestParam("state") String state
    ) {
        System.out.println(code);
        return Result.SUCCESS();
    }

    private AccessTokenResult getAccessToken(String code) {
        String url = accessTokenUrl + "?appid=" + appId + "&secret=" + appSecret + "&code=" + code + "&grant_type=authorization_code";
        AccessTokenResult result = restTemplate.getForObject(url, AccessTokenResult.class);

        System.out.println(result);
        if (result == null || result.getAccess_token() == null) return null;
        return result;
    }

    private UserInfoResult getWxUserInfo(String access_token, String openid) {
        String url = userInfoUrl + "?access_token=" + access_token + "&openid=" + openid + "&lang=zh_CN";
        UserInfoResult userInfoResult = restTemplate.getForObject(url, UserInfoResult.class);
        System.out.println(userInfoResult);
        if (userInfoResult == null || userInfoResult.getErrcode() == null)
            return null;
        return userInfoResult;
    }
}
