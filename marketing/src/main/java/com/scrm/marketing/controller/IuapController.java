package com.scrm.marketing.controller;

import com.scrm.marketing.share.iuap.AccessToken;
import com.scrm.marketing.share.iuap.JsTicket;
import com.scrm.marketing.share.iuap.UserInfoResult;
import com.scrm.marketing.util.MyLoggerUtil;
import com.scrm.marketing.util.resp.CodeEum;
import com.scrm.marketing.util.resp.Result;
import lombok.Data;
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
 * 用友iuap相关的controller
 *
 * @author fzk
 * @date 2021-11-18 20:12
 */
@RestController
@RequestMapping("/mk/iuap")
public class IuapController {

    @Value("${my.iuap.appid}")
    private String appid;
    @Value("${my.iuap.secret}")
    private String secret;

    private final RestTemplate restTemplate;

    public IuapController() {
        /* 对于微信返回的Content-Type为text/plain的处理 */
        restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN, MediaType.APPLICATION_OCTET_STREAM));
        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
    }

    /**
     * 使用空间的用户作为免登数据依据。比如：空间用户ID、手机号等
     * 请求地址：https://openapi.yonyoucloud.com/certified/userInfo/{{:code}}?access_token=xxx
     *
     * @param code 用户在点击轻应用之后跳转的URL会自动附加code参数，此code由友空间统一生成，以此来作为用户的授权登录key
     * @return result
     */
    @GetMapping("/userInfo")
    public Result getUserInfo(@RequestParam("code") String code) {
        // 1.获取access_token
        String access_token = getAccessToken();

        // 2.获取用户信息
        String url = "https://openapi.yonyoucloud.com/certified/userInfo/" + code + "?access_token=" + access_token;
        UserInfoResult infoResult = restTemplate.getForObject(url, UserInfoResult.class);

        if (infoResult != null && "0".equals(infoResult.getCode())) {
            return Result.success(infoResult.getData());
        }

        return Result.error(CodeEum.FAIL, infoResult == null ? "没拿到任何信息" : infoResult.getMsg());
    }

    /**
     * 用友JSAPI鉴权
     *
     * @return result
     */
    @GetMapping("/jsApi")
    public Result jsapi() {
        // 1.获取js_ticket
        String js_ticket = getJs_ticket();

        if (js_ticket == null) return Result.FAIL();

        // 2.获取时间戳
        long timestamp = System.currentTimeMillis();

        // 3.加密
        String signature = JsTicket.signature(appid, js_ticket, timestamp);

        // 4.包装结果
        JsTicketWrapper jsTicketWrapper = new JsTicketWrapper(appid, timestamp, signature);
        return Result.success(jsTicketWrapper);
    }

    /**
     * 令牌是后续访问友空间所有API接口的凭证
     * 请求地址：https://openapi.yonyoucloud.com/token?appid=xxx&secret=xxx
     *
     * @return access_token 或者 null
     */
    public String getAccessToken() {
        // 1.先从缓存拿
        if (AccessToken.AccessTokenHolder.isValid())
            return AccessToken.AccessTokenHolder.accessTokenCache.getAccess_token();

        // 2.可能是过期了，可能是还没有初始化拿过：双重校验锁DCL
        synchronized (AccessToken.AccessTokenHolder.class) {
            // 2.1 再检查一下缓存
            if (AccessToken.AccessTokenHolder.isValid())
                return AccessToken.AccessTokenHolder.accessTokenCache.getAccess_token();

            // 2.2 确实没有，发get请求去拿access_token
            String url = "https://openapi.yonyoucloud.com/token?appid=" + appid + "&secret=" + secret;
            AccessToken accessToken = restTemplate.getForObject(url, AccessToken.class);

            // 2.3 把新的access_token放入缓存
            AccessToken.AccessTokenHolder accessTokenHolder;
            if (accessToken != null && (accessTokenHolder = accessToken.getData()) != null &&
                    accessTokenHolder.getAccess_token() != null) {
                // 默认7000s后重新获取access_token
                accessTokenHolder.setValidBefore(System.currentTimeMillis() + (accessTokenHolder.getExpiresIn() - 200L) * 1000L);
                AccessToken.AccessTokenHolder.accessTokenCache = accessTokenHolder;
                return accessTokenHolder.getAccess_token();
            }
        }
        // 打个日志
        MyLoggerUtil.warning("竟然没拿到用友iuap的access_token?");
        return null;
    }

    /**
     * 请求地址：https://openapi.yonyoucloud.com/app-sdk/rest/js/ticket?access_token=xxx
     *
     * @return js_ticket 或 null
     */
    private String getJs_ticket() {
        // 1.先去缓存拿
        if (JsTicket.JsTicketHolder.isValid())
            return JsTicket.JsTicketHolder.jsTicketCache.getJs_ticket();

        // 2.可能是过期了，可能是还没有初始化拿过：双重校验锁DCL
        synchronized (JsTicket.JsTicketHolder.class) {
            // 2.1.再去缓存拿
            if (JsTicket.JsTicketHolder.isValid())
                return JsTicket.JsTicketHolder.jsTicketCache.getJs_ticket();

            // 2.2 确实没有，发get请求去拿js_ticket
            String access_token = getAccessToken();
            if (access_token == null) return null;

            String url = "https://openapi.yonyoucloud.com/app-sdk/rest/js/ticket?access_token=" + access_token;
            JsTicket jsTicket = restTemplate.getForObject(url, JsTicket.class);

            // 2.3 把新的js_ticket放入缓存
            JsTicket.JsTicketHolder jsTicketHolder;
            if (jsTicket != null && (jsTicketHolder = jsTicket.getData()) != null && jsTicketHolder.getJs_ticket() != null) {
                jsTicketHolder.setValidBefore(System.currentTimeMillis() + (jsTicketHolder.getExpires_in() - 200L) * 1000L);
                JsTicket.JsTicketHolder.jsTicketCache = jsTicketHolder;
                return jsTicketHolder.getJs_ticket();
            }
            // 打个日志
            MyLoggerUtil.warning(() -> "竟然没拿到用友iuap的js_ticket?");
            return null;
        }
    }

    /**
     * 包装类，用于返回结果
     */
    @Data
    public static class JsTicketWrapper {
        private String appid;
        private long timestamp;
        private String signature;
        public JsTicketWrapper(String appid,long timestamp,String signature){
            this.appid=appid;
            this.timestamp=timestamp;
            this.signature=signature;
        }
    }

    /*
    @GetMapping("/js_ticket")
    public Result getJsTicket() throws NoSuchAlgorithmException, InvalidKeyException {
        String js_ticket = getJs_ticket();
        return Result.success(js_ticket);
    }

    private String getAccessToken() throws NoSuchAlgorithmException, InvalidKeyException {
        // 1.先从缓存拿
        if (AccessToken.AccessTokenHolder.isValid())
            return AccessToken.AccessTokenHolder.accessTokenCache.getAccess_token();

        // 2.可能是过期了，可能是还没有初始化拿过：双重校验锁DCL
        synchronized (AccessToken.AccessTokenHolder.class) {
            // 2.1 再检查一下缓存
            if (AccessToken.AccessTokenHolder.isValid())
                return AccessToken.AccessTokenHolder.accessTokenCache.getAccess_token();

            // 2.2 确实没有，发get请求去拿access_token
            // "https://api.diwork.com/open-auth/selfAppAuth/getAccessToken?appKey=xxx&timestamp=xxx&signature=xxx"
            long timestamp = System.currentTimeMillis();
            String signature = AccessToken.signature(appKey, appSecret, timestamp);
            String url = "https://api.diwork.com/open-auth/selfAppAuth/getAccessToken?" +
                    "appKey=" + appKey +
                    "&timestamp=" + timestamp +
                    "&signature=" + signature;

            AccessToken accessToken = restTemplate.getForObject(url, AccessToken.class);

            AccessToken.AccessTokenHolder accessTokenHolder;
            if (accessToken != null && (accessTokenHolder = accessToken.getData()) != null &&
                    accessTokenHolder.getAccess_token() != null) {
                // 默认7000s后重新获取access_token
                accessTokenHolder.setValidBefore(System.currentTimeMillis() + (accessTokenHolder.getExpire() - 200L) * 1000L);
                AccessToken.AccessTokenHolder.accessTokenCache = accessTokenHolder;
                return accessTokenHolder.getAccess_token();
            }
            // 打个日志
            MyLoggerUtil.warning(() -> "竟然没拿到用友iuap的access_token?");
            return null;
        }
    }

    private String getJs_ticket() throws NoSuchAlgorithmException, InvalidKeyException {
        // 1.先去缓存拿
        if (JsTicket.JsTicketHolder.isValid())
            return JsTicket.JsTicketHolder.jsTicketCache.getJs_ticket();

        // 2.可能是过期了，可能是还没有初始化拿过：双重校验锁DCL
        synchronized (JsTicket.JsTicketHolder.class) {
            // 2.1.再去缓存拿
            if (JsTicket.JsTicketHolder.isValid())
                return JsTicket.JsTicketHolder.jsTicketCache.getJs_ticket();

            // 2.2 确实没有，发get请求去拿js_ticket
            // "https://api.diwork.com/yonbip/uspace/jsbridge/jsticket?access_token=xxx"
            String access_token = getAccessToken();
            if (access_token == null) return null;

            String url = "https://api.diwork.com/yonbip/uspace/jsbridge/jsticket?access_token=" + access_token;
            JsTicket jsTicket = restTemplate.getForObject(url, JsTicket.class);

            JsTicket.JsTicketHolder jsTicketHolder;
            if (jsTicket != null && (jsTicketHolder = jsTicket.getData()) != null && jsTicketHolder.getJs_ticket() != null) {
                jsTicketHolder.setValidBefore(System.currentTimeMillis() + (jsTicketHolder.getExpires_in() - 200L) * 1000L);
                JsTicket.JsTicketHolder.jsTicketCache = jsTicketHolder;
                return jsTicketHolder.getJs_ticket();
            }
            // 打个日志
            MyLoggerUtil.warning(() -> "竟然没拿到用友iuap的js_ticket?");
            return null;
        }
    }*/
}
