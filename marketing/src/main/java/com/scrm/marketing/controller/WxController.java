package com.scrm.marketing.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.scrm.marketing.service.WxUserService;
import com.scrm.marketing.share.wx.UserInfoAccessToken;
import com.scrm.marketing.util.MyLoggerUtil;
import com.scrm.marketing.util.MyRandomUtil;
import com.scrm.marketing.util.resp.CodeEum;
import com.scrm.marketing.util.resp.Result;
import com.scrm.marketing.share.wx.GlobalAccessToken;
import com.scrm.marketing.share.wx.JsapiTicket;
import com.scrm.marketing.share.wx.WxUserInfoResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
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
    @Value("${my.wx.token}")
    private String token;

    @Resource
    private WxUserService wxUserService;

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

        else MyLoggerUtil.warning("=================warning: signature校验失败，不过还是直接返回echostr=====================");
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
    public Result getWxUserInfo(
            @RequestParam("code") String code,
            @RequestParam("state") String state
    ) {
        // 1.根据code获取access_token
        UserInfoAccessToken userInfoAccessToken = getWxAccessToken(code);
        if (userInfoAccessToken.getAccess_token() == null)
            return Result.error(CodeEum.ERROR, "获取access_token失败: " +
                    "errcode:" + userInfoAccessToken.getErrcode() +
                    "errmsg:" + userInfoAccessToken.getErrmsg());
        //System.out.println("获取到的access_token: " + userInfoAccessToken);

        // 2.根据access_token获取userinfo
        WxUserInfoResult wxUserInfo = doGetWxUserInfo(userInfoAccessToken.getAccess_token(), userInfoAccessToken.getOpenid());
        if (wxUserInfo.getOpenid() == null)
            return Result.error(CodeEum.ERROR, "获取微信用户信息失败: " +
                    "errcode:" + wxUserInfo.getErrcode() +
                    "errmsg:" + wxUserInfo.getErrmsg());
        //System.out.println("获取到的微信用户信息: " + wxUserInfo);

        // 3.state处理
        /*暂时没有用到state*/

        // 4.保存最新的用户信息到数据库
        wxUserService.saveWxUser(wxUserInfo);

        return Result.success(wxUserInfo);
    }

    @GetMapping("/jsApi")
    public Result jsapi_ticket(@RequestParam("url") String url) {
        // 1.获取access_token
        GlobalAccessToken accessToken = getGlobalAccessToken();
        if (accessToken.getAccess_token() == null)
            return Result.error(CodeEum.ERROR, "获取access_token失败: " +
                    "errcode:" + accessToken.getErrcode() +
                    "errmsg:" + accessToken.getErrmsg());

        // 2.获取jsapi_ticket
        JsapiTicket jsapi_ticket = getJsapi_ticket(accessToken.getAccess_token());
        if (jsapi_ticket.getTicket() == null)
            return Result.error(CodeEum.ERROR, "获取access_token失败: " +
                    "errcode:" + accessToken.getErrcode() +
                    "errmsg:" + accessToken.getErrmsg());

        // 3.根据ticket 以及 其他参数 sha1加密生成signature
        String noncestr = MyRandomUtil.randomStr(16);
        long timestamp = System.currentTimeMillis();
        String signature = JsapiTicket.ticket_signature(jsapi_ticket.getTicket(), noncestr, timestamp, url);

        // 4.返回
        JsapiTicket.TicketSignatureWrapper ticketSignatureWrapper = new JsapiTicket.TicketSignatureWrapper(appId, signature, noncestr, timestamp);
        return Result.success(ticketSignatureWrapper);
    }

    /*确保返回的AccessTokenResult不为null*/
    private UserInfoAccessToken getWxAccessToken(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appSecret + "&code=" + code + "&grant_type=authorization_code";
        UserInfoAccessToken result = restTemplate.getForObject(url, UserInfoAccessToken.class);

//        if (result == null || result.getAccess_token() == null) return null;
        if (result == null) {
            result = new UserInfoAccessToken();
            result.setErrmsg("啥都没拿到，获取access_token的调用返回为null?");
        }
        return result;
    }

    /*确保返回的WxUserInfoResult不为null*/
    private WxUserInfoResult doGetWxUserInfo(String access_token, String openid) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid + "&lang=zh_CN";
        WxUserInfoResult userInfoResult = restTemplate.getForObject(url, WxUserInfoResult.class);
//        if (userInfoResult == null || userInfoResult.getErrcode() != null) return null;
        if (userInfoResult == null) {
            userInfoResult = new WxUserInfoResult();
            userInfoResult.setErrmsg("啥都没拿到，获取微信用户信息的调用返回为null");
        }
        return userInfoResult;
    }

    /*这里也确保返回的GlobalAccessToken不为null*/
    private GlobalAccessToken getGlobalAccessToken() {
        if (globalAccessToken.getAccess_token() != null && System.currentTimeMillis() < globalAccessToken.getValidBefore())
            return globalAccessToken;
        else {
            synchronized (WxController.globalAccessToken) {
                if (globalAccessToken.getAccess_token() != null && System.currentTimeMillis() < globalAccessToken.getValidBefore())
                    return globalAccessToken;

                // "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
                String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret;
                GlobalAccessToken accessToken = restTemplate.getForObject(url, GlobalAccessToken.class);

                if (accessToken == null) {
                    accessToken = new GlobalAccessToken();
                    accessToken.setErrmsg("啥也没拿到，获取access_token的调用返回了null");
                    return accessToken;
                }
                // 确保错误信息的传递
                if (accessToken.getAccess_token() == null) return accessToken;

                globalAccessToken.setAccess_token(accessToken.getAccess_token());
                globalAccessToken.setExpires_in(accessToken.getExpires_in());
                globalAccessToken.setValidBefore(System.currentTimeMillis() + (accessToken.getExpires_in() - 200) * 1000);
                return globalAccessToken;
            }
        }
    }

    /*确保返回的JsapiTicket不为null*/
    private JsapiTicket getJsapi_ticket(String accessToken) {
        if (global_jsapi_ticket.getTicket() != null && System.currentTimeMillis() < global_jsapi_ticket.getValidBefore())
            return global_jsapi_ticket;
        else {
            synchronized (WxController.global_jsapi_ticket) {
                if (global_jsapi_ticket.getTicket() != null && System.currentTimeMillis() < global_jsapi_ticket.getValidBefore())
                    return global_jsapi_ticket;

                // "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
                String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=jsapi";
                JsapiTicket ticket = restTemplate.getForObject(url, JsapiTicket.class);

                if (ticket == null) {
                    ticket = new JsapiTicket();
                    ticket.setErrmsg("jsapi ticket获取什么都没拿到，调用返回为null");
                    return ticket;
                }
                if (ticket.getTicket() == null) return ticket;

                global_jsapi_ticket.setTicket(ticket.getTicket());
                global_jsapi_ticket.setExpires_in(ticket.getExpires_in());
                global_jsapi_ticket.setValidBefore(System.currentTimeMillis() + (ticket.getExpires_in() - 200) * 1000);
                return global_jsapi_ticket;
            }
        }
    }
}
