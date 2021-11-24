package com.scrm.marketing.share.iuap;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.MD5;
import com.scrm.marketing.util.MyDigestUtil;
import lombok.Data;

import java.nio.charset.StandardCharsets;

/**
 * GET请求地址：https://api.diwork.com/yonbip/uspace/jsbridge/jsticket?access_token=xxx
 *
 * @author fzk
 * @date 2021-11-18 17:05
 */
@Data
public class JsTicket {
    private String error_code; // 成功为 "0"
    private String error; // 成功为 "success"
    private String error_description; // 成功为 ""
    private JsTicketHolder data;


    @Data
    public static class JsTicketHolder {
        private String js_ticket;
        private Integer expires_in; //过期时间，单位s，默认604800，即1周

        private long validBefore; // 默认应该是得到js_ticket时间+(604800-200)s的时间戳

        /*缓存*/
        public static volatile JsTicketHolder jsTicketCache = null;

        /*判断缓存里的js_ticket是否有效*/
        public static boolean isValid() {
            return jsTicketCache != null &&
                    System.currentTimeMillis() < jsTicketCache.getValidBefore();
        }
    }

    public static String signature(String agentId, String jsTicket, long timeStamp) {
        // 1.三个参数按照如下拼接
        String to_signature = "agentId=" + agentId + "&jsTicket=" + jsTicket + "&timeStamp=" + timeStamp;
        // 2.使用md5加密方式对拼接字符串加密
        return MyDigestUtil.md5Hex(to_signature);
    }
}
