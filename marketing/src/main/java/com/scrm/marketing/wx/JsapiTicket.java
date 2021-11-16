package com.scrm.marketing.wx;

import lombok.Data;

/**
 * @author fzk
 * @date 2021-11-15 21:00
 */
@Data
public class JsapiTicket {
    private String ticket;
    private Long expires_in;

    /**
     * 这个居然是无论有没有异常，都会返回这两个参数
     */
    @SuppressWarnings("all")
    private Integer errcode;
    @SuppressWarnings("all")
    private String errmsg;

    private long validBefore;//默认应该是得到ticket时间+7000s的时间戳
}
