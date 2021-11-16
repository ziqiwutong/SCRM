package com.scrm.marketing.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.scrm.marketing.util.MyRandomUtil;
import com.scrm.marketing.util.resp.CodeEum;
import com.scrm.marketing.util.resp.Result;
import com.scrm.marketing.wx.AccessTokenResult;
import com.scrm.marketing.wx.GlobalAccessToken;
import com.scrm.marketing.wx.JsapiTicket;
import com.scrm.marketing.wx.WxUserInfoResult;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class WxController {

    @Value("${my.wx.appId}")
    private String appId;
    @Value("${my.wx.appSecret}")
    private String appSecret;
    @Value("${my.wx.token}")
    private String token;

    private static final String oauthAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
    private static final String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo";

    private static final String globalAccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token";
    private static final String jsapi_ticketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";

    private static final GlobalAccessToken globalAccessToken = new GlobalAccessToken();
    private static final JsapiTicket global_jsapi_ticket = new JsapiTicket();

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
        // 1.按字典序排序：nonce,timestamp,token，拼接字符串
        String s = "nonce=" + nonce + "&timestamp=" + timestamp + "&token=" + token;

        // 2.sha1加密之后的字符串可与signature对比，标识该请求来源于微信
        if (signature.equals(DigestUtil.sha1Hex(s))) return echostr;// 成功则原样返回

        else System.out.println("=================warning: signature校验失败，不过还是直接返回echostr=====================");
        log.error("signature校验失败，不过还是直接返回echostr");
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

        // 3.state处理
        /*暂时没有用到state*/

        return Result.success(wxUserInfo);
    }

    @GetMapping("/jsApi")
    public Result jsapi_ticket(@RequestParam("url") String url) {
        System.out.println("传入的url: " + url);
        // 1.获取access_token
        GlobalAccessToken accessToken = getGlobalAccessToken();
        if (accessToken == null || accessToken.getAccess_token() == null)
            return Result.error(CodeEum.ERROR);

        // 2.获取jsapi_ticket
        JsapiTicket jsapi_ticket = getJsapi_ticket(accessToken.getAccess_token());
        if (jsapi_ticket == null || jsapi_ticket.getTicket() == null)
            return Result.error(CodeEum.ERROR);

        // 3.根据ticket 以及 其他参数 sha1加密生成signature
        String noncestr = MyRandomUtil.randomStr(16);
        long timestamp = System.currentTimeMillis();
        String signature = ticket_signature(jsapi_ticket.getTicket(), noncestr, timestamp, url);

        // 4.返回
        TicketSignatureWrapper ticketSignatureWrapper = new TicketSignatureWrapper(appId, signature, noncestr, timestamp);
        return Result.success(ticketSignatureWrapper);
    }

    private AccessTokenResult getAccessToken(String code) {
        String url = oauthAccessTokenUrl + "?appid=" + appId + "&secret=" + appSecret + "&code=" + code + "&grant_type=authorization_code";
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


    private GlobalAccessToken getGlobalAccessToken() {
        if (globalAccessToken.getAccess_token() != null && System.currentTimeMillis() < globalAccessToken.getValidBefore())
            return globalAccessToken;
        else {
            synchronized (WxController.globalAccessToken) {
                if (globalAccessToken.getAccess_token() != null && System.currentTimeMillis() < globalAccessToken.getValidBefore())
                    return globalAccessToken;

                // "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
                String url = globalAccessTokenUrl + "?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret;
                GlobalAccessToken accessToken = restTemplate.getForObject(url, GlobalAccessToken.class);
                if (accessToken == null || accessToken.getAccess_token() == null)
                    return null;

                globalAccessToken.setAccess_token(accessToken.getAccess_token());
                globalAccessToken.setExpires_in(accessToken.getExpires_in());
                globalAccessToken.setValidBefore(System.currentTimeMillis() + (accessToken.getExpires_in() - 200) * 1000);
                return globalAccessToken;
            }
        }
    }


    private JsapiTicket getJsapi_ticket(String accessToken) {
        if (global_jsapi_ticket.getTicket() != null && System.currentTimeMillis() < global_jsapi_ticket.getValidBefore())
            return global_jsapi_ticket;
        else {
            synchronized (WxController.global_jsapi_ticket) {
                if (global_jsapi_ticket.getTicket() != null && System.currentTimeMillis() < global_jsapi_ticket.getValidBefore())
                    return global_jsapi_ticket;

                // "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
                String url = jsapi_ticketUrl + "?access_token=" + accessToken + "&type=jsapi";
                JsapiTicket ticket = restTemplate.getForObject(url, JsapiTicket.class);
                if (ticket == null || ticket.getTicket() == null) return null;

                global_jsapi_ticket.setTicket(ticket.getTicket());
                global_jsapi_ticket.setExpires_in(ticket.getExpires_in());
                global_jsapi_ticket.setValidBefore(System.currentTimeMillis() + (ticket.getExpires_in() - 200) * 1000);
                return global_jsapi_ticket;
            }
        }
    }

    private String ticket_signature(String jsapi_ticket, String noncestr, long timestamp, String url) {
        String str = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + url;
        // sha1加密
        return DigestUtil.sha1Hex(str);
    }

    @SuppressWarnings("unused")
    private static class TicketSignatureWrapper {
        private String appId;
        private String signature;
        private String noncestr;
        private long timestamp;

        public TicketSignatureWrapper(String appId, String signature, String noncestr, long timestamp) {
            this.appId = appId;
            this.signature = signature;
            this.noncestr = noncestr;
            this.timestamp = timestamp;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
}
