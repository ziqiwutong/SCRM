package com.scrm.marketing.controller;

import com.scrm.marketing.share.iuap.AccessToken;
import com.scrm.marketing.share.iuap.JsTicket;
import com.scrm.marketing.util.MyLoggerUtil;
import com.scrm.marketing.util.resp.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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

    @Value("${my.iuap.appKey}")
    private String appKey;
    @Value("${my.iuap.appSecret}")
    private String appSecret;

    private final RestTemplate restTemplate;

    public IuapController() {
        /* 对于微信返回的Content-Type为text/plain的处理 */
        restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN));
        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
    }

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
    }
}
