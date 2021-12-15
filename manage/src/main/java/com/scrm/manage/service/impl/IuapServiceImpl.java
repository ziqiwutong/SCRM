package com.scrm.manage.service.impl;

import com.scrm.manage.exception.MyException;
import com.scrm.manage.service.IuapService;
import com.scrm.manage.share.iuap.AccessToken;
import com.scrm.manage.share.iuap.JsTicket;
import com.scrm.manage.share.iuap.UserInfoResult;
import com.scrm.manage.util.resp.CodeEum;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * @author fzk
 * @date 2021-11-18 20:12
 */
@Service
public class IuapServiceImpl implements IuapService {

    @Value("${my.iuap.appid}")
    private String appid;
    @Value("${my.iuap.secret}")
    private String secret;

    private final RestTemplate restTemplate;

    public IuapServiceImpl() {
        /* 对于微信返回的Content-Type为text/plain的处理 */
        restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN, MediaType.APPLICATION_OCTET_STREAM));
        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
    }

    /**
     * 令牌是后续访问友空间所有API接口的凭证
     * 请求地址：https://openapi.yonyoucloud.com/token?appid=xxx&secret=xxx
     *
     * @return access_token,不会为null
     */
    @Override
    public String getAccessToken() {
        // 1.先从缓存拿
        if (AccessToken.AccessTokenHolder.isValid())
            return AccessToken.AccessTokenHolder.accessTokenCache.getAccess_token();
        AccessToken accessToken;

        // 2.可能是过期了，可能是还没有初始化拿过：双重校验锁DCL
        synchronized (AccessToken.AccessTokenHolder.class) {
            // 2.1 再检查一下缓存
            if (AccessToken.AccessTokenHolder.isValid())
                return AccessToken.AccessTokenHolder.accessTokenCache.getAccess_token();

            // 2.2 确实没有，发get请求去拿access_token
            String url = "https://openapi.yonyoucloud.com/token?appid=" + appid + "&secret=" + secret;
            accessToken = restTemplate.getForObject(url, AccessToken.class);

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
        // 3. 获取失败，抛异常
        throw new MyException(CodeEum.CODE_ERROR,
                accessToken == null ? "accessToken获取失败且未返回任何信息" :
                        accessToken.getCode() + ":" + accessToken.getMsg());
    }

    @Override
    public UserInfoResult getUserInfo(String code) {
        // 1.获取access_token
        String access_token = getAccessToken();

        // 2.获取用户信息
        String url = "https://openapi.yonyoucloud.com/certified/userInfo/" + code + "?access_token=" + access_token;
        return restTemplate.getForObject(url, UserInfoResult.class);
    }

    @Override
    public Object jsTicket() {
        // 1.获取js_ticket,确保返回不会为null
        String js_ticket = getJs_ticket();

        // 2.获取时间戳
        long timestamp = System.currentTimeMillis();

        // 3.加密
        String signature = JsTicket.signature(appid, js_ticket, timestamp);

        // 4.包装结果
        return new JsTicketWrapper(appid, timestamp, signature);
    }

    /**
     * 请求地址：https://openapi.yonyoucloud.com/app-sdk/rest/js/ticket?access_token=xxx
     *
     * @return js_ticket,不会为null
     */
    private String getJs_ticket() {
        // 1.先去缓存拿
        if (JsTicket.JsTicketHolder.isValid())
            return JsTicket.JsTicketHolder.jsTicketCache.getJs_ticket();
        JsTicket jsTicket;

        // 2.可能是过期了，可能是还没有初始化拿过：双重校验锁DCL
        synchronized (JsTicket.JsTicketHolder.class) {
            // 2.1.再去缓存拿
            if (JsTicket.JsTicketHolder.isValid())
                return JsTicket.JsTicketHolder.jsTicketCache.getJs_ticket();

            // 2.2 确实没有，发get请求去拿js_ticket
            String access_token = getAccessToken();//先获取access_token，这里要确保返回不会为null

            String url = "https://openapi.yonyoucloud.com/app-sdk/rest/js/ticket?access_token=" + access_token;
            jsTicket = restTemplate.getForObject(url, JsTicket.class);

            // 2.3 把新的js_ticket放入缓存
            JsTicket.JsTicketHolder jsTicketHolder;
            if (jsTicket != null && (jsTicketHolder = jsTicket.getData()) != null && jsTicketHolder.getJs_ticket() != null) {
                jsTicketHolder.setValidBefore(System.currentTimeMillis() + (jsTicketHolder.getExpires_in() - 200L) * 1000L);
                JsTicket.JsTicketHolder.jsTicketCache = jsTicketHolder;
                return jsTicketHolder.getJs_ticket();
            }
        }
        // 3.没拿到，抛异常
        throw new MyException(CodeEum.CODE_ERROR,
                jsTicket == null ? "啥都没拿到" :
                        jsTicket.getError_code() + ":" + jsTicket.getError_description());
    }

    /**
     * 包装类，用于返回结果
     */
    @Data
    public static class JsTicketWrapper {
        private String appid;
        private long timestamp;
        private String signature;

        public JsTicketWrapper(String appid, long timestamp, String signature) {
            this.appid = appid;
            this.timestamp = timestamp;
            this.signature = signature;
        }
    }
}
