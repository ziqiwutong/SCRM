package com.scrm.marketing.share.wx;

import cn.hutool.crypto.digest.DigestUtil;
import lombok.Data;

/**
 * @author fzk
 * @date 2021-11-15 21:00
 */
@Data
public class JsapiTicket {
    /**
     * jsapi_ticket是公众号用于调用微信JS接口的临时票据。
     * 正常情况下，jsapi_ticket的有效期为7200秒，通过access_token来获取。
     * 由于获取jsapi_ticket的api调用次数非常有限，频繁刷新jsapi_ticket会导致api调用受限，
     * 影响自身业务，开发者必须在自己的服务全局缓存jsapi_ticket
     */
    private String ticket;//jsapi_ticket是公众号用于调用微信JS接口的临时票据
    private Long expires_in;//jsapi_ticket的有效期，单位s，默认7200

    /**
     * 这个居然是无论有没有异常，都会返回这两个参数
     */
    @SuppressWarnings("all")
    private Integer errcode;
    @SuppressWarnings("all")
    private String errmsg;

    private long validBefore;//默认应该是得到ticket时间+7000s的时间戳


    /**
     * 获得jsapi_ticket之后，就可以生成JS-SDK权限验证的签名了
     * 对所有待签名参数按照字段名的ASCII 码从小到大排序（字典序）后，
     * 使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串string1
     * 这里需要注意的是所有参数名均为小写字符。
     * 对string1作sha1加密，字段名和字段值都采用原始值，不进行URL 转义。
     *
     * @param jsapi_ticket 有效的jsapi_ticket
     * @param noncestr     随机字符串
     * @param timestamp    时间戳
     * @param url          当前网页的URL，不包含#及其后面部分
     * @return sha1加密后的字符串
     */
    public static String ticket_signature(String jsapi_ticket, String noncestr, long timestamp, String url) {
        String str = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + url;
        // sha1加密
        return DigestUtil.sha1Hex(str);
    }

    /**
     * 用于返回给前端的包装类：包装了前端调用JS-SDK所需要属性
     */
    @Data
    public static class TicketSignatureWrapper {
        private String appId;
        private String signature;// sha1加密后的密文：根据ticket,timestamp,noncestr,url进行sha1加密
        private String noncestr;// 随机字符串
        private long timestamp;//时间戳

        public TicketSignatureWrapper(String appId, String signature, String noncestr, long timestamp) {
            this.appId = appId;
            this.signature = signature;
            this.noncestr = noncestr;
            this.timestamp = timestamp;
        }
    }
}
