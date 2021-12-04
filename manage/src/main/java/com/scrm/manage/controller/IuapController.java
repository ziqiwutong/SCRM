package com.scrm.manage.controller;

import com.scrm.manage.exception.MyException;
import com.scrm.manage.share.iuap.AccessToken;
import com.scrm.manage.share.iuap.JsTicket;
import com.scrm.manage.share.iuap.UserInfoResult;
import com.scrm.manage.util.resp.CodeEum;
import com.scrm.manage.util.resp.Result;
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
@RequestMapping("/cms/iuap")
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
        // 1.获取js_ticket,确保返回不会为null
        String js_ticket = getJs_ticket();

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
     * @return access_token,不会为null
     */
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
