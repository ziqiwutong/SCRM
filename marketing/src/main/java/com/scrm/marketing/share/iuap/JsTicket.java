package com.scrm.marketing.share.iuap;

import lombok.Data;

/**
 * GET请求地址：https://api.diwork.com/yonbip/uspace/jsbridge/jsticket?access_token=xxx
 *
 * @author fzk
 * @date 2021-11-18 17:05
 */
@Data
public class JsTicket {
    private String code;
    private JsTicketHolder data;


    @Data
    public static class JsTicketHolder {
        private String js_ticket;
        private Integer expires_in; //过期时间，单位s，默认604800，即1周

        private long validBefore; // 默认应该是得到js_ticket时间+(604800-200)s的时间戳

        /*缓存*/
        public static JsTicketHolder jsTicketCache = null;

        /*判断缓存里的js_ticket是否有效*/
        public static boolean isValid() {
            return jsTicketCache != null &&
                    System.currentTimeMillis() < jsTicketCache.getValidBefore();
        }
    }
}
