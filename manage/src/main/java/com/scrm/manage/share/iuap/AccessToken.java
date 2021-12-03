package com.scrm.manage.share.iuap;

import com.scrm.manage.util.MyDigestUtil;
import lombok.Data;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 调用接口令牌 access_token 是应用调用开放平台业务接口的凭证，有效期为2小时,过期后需要重新获取
 * 请求地址：https://openapi.yonyoucloud.com/token?appid=exx&secret=xxx
 *
 * @author fzk
 * @date 2021-11-18 17:06
 */
@Data
public class AccessToken {
    private String code; // 结果码，正确返回 "0"
    private String msg; // 结果信息，若有错误，该字段会返回具体错误信息
    private AccessTokenHolder data;

    @Data
    public static class AccessTokenHolder {
        private String access_token; // 接口令牌 access_token
        private Integer expiresIn; // 有效期，单位秒, 默认7200

        private long validBefore; // 默认应该是得到access_token时间+7000s的时间戳


        /*缓存*/
        public static volatile AccessTokenHolder accessTokenCache = null;

        /*判断缓存里的access_token是否有效*/
        public static boolean isValid() {
            return accessTokenCache != null &&
                    System.currentTimeMillis() < accessTokenCache.getValidBefore();
        }
    }

    /**
     * 签名字段signature计算使用HmacSHA256
     * 具体计算方式如下： URLEncode( Base64( HmacSHA256( parameterMap ) ) )
     * parameterMap 按照参数名称排序，参数名称与参数值依次拼接(signature字段除外)
     *
     * @param appKey    自建应用的appKey
     * @param appSecret 自建应用的appSecret
     * @param timestamp 时间戳
     * @return String
     */
    public static String signature(String appKey, String appSecret, long timestamp) throws NoSuchAlgorithmException, InvalidKeyException {
        // 1.拼接parameterMap
        String parameterMap = "appKey" + appKey + "timestamp" + timestamp;
        // 2.对 parameterMap 使用 HmacSHA256 计算签名，Hmac 的 key 为自建应用的 appSecret
        String origin_signature = MyDigestUtil.sha256_mac(parameterMap, appSecret);

        // 3.计算出的二进制签名先进行 base64
        String base64_signature = Base64.getEncoder().encodeToString(origin_signature.getBytes(StandardCharsets.UTF_8));

        // 4.之后进行 urlEncode，即得到 signature 字段的值
        return URLEncoder.encode(base64_signature, StandardCharsets.UTF_8);
    }

}
