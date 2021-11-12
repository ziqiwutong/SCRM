package com.scrm.marketing.controller;

import com.scrm.marketing.util.resp.CodeEum;
import com.scrm.marketing.util.resp.Result;
import com.scrm.marketing.wx.AccessTokenResult;
import com.scrm.marketing.wx.WxUserInfoResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * @author fzk
 * @date 2021-11-07 19:28
 */
@RestController
@RequestMapping("/mk/wx")
public class WxController {

    @Value("${my.wx.appId}")
    private String appId;
    @Value("${my.wx.appSecret}")
    private String appSecret;
    //    private static final String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    private static final String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
    //    private static final String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    private static final String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo";
    //    @Resource
    private final RestTemplate restTemplate;

    public WxController() {
        /* 对于微信返回的Content-Type为text/plain的处理 */
        restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN));
        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
    }

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

    /**
     * 微信用户授权后的回调接口，会返回code和state
     *
     * @param code  code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期
     * @param state 重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
     * @return result
     */
    @GetMapping("/getWxUserInfo")
    public Result getCode(
            @RequestParam("code") String code,
            @RequestParam("state") String state
    ) {
        System.out.println("获取到的code: " + code);
        // 1.根据code获取access_token
        AccessTokenResult accessTokenResult = getAccessToken(code);
        if (accessTokenResult == null) return Result.error(CodeEum.ERROR, "获取access_token失败");
        System.out.println("获取到的access_token: " + accessTokenResult);

        // 2.根据access_token获取userinfo
        WxUserInfoResult wxUserInfo = getWxUserInfo(accessTokenResult.getAccess_token(), accessTokenResult.getOpenid());
        if (wxUserInfo == null) return Result.error(CodeEum.ERROR, "获取微信用户信息失败");
        System.out.println("获取到的微信用户信息: " + wxUserInfo);

        return Result.success(wxUserInfo);
    }

    private AccessTokenResult getAccessToken(String code) {
        String url = accessTokenUrl + "?appid=" + appId + "&secret=" + appSecret + "&code=" + code + "&grant_type=authorization_code";
        AccessTokenResult result = restTemplate.getForObject(url, AccessTokenResult.class);

        if (result == null || result.getAccess_token() == null) return null;
        return result;
    }

    private WxUserInfoResult getWxUserInfo(String access_token, String openid) {
        String url = userInfoUrl + "?access_token=" + access_token + "&openid=" + openid + "&lang=zh_CN";
        WxUserInfoResult userInfoResult = restTemplate.getForObject(url, WxUserInfoResult.class);
        if (userInfoResult == null || userInfoResult.getErrcode() != null)
            return null;
        return userInfoResult;
    }
}
