package com.scrm.marketing.share.wx;

import lombok.Data;

/**
 * @author fzk
 * @date 2021-11-09 17:05
 */
@Data
public class WxAccessToken {
    /**
     * 请求成功的属性
     */
    private String access_token;
    private Integer expires_in;
    private String refresh_token;
    private String openid;
    private String scope;

    /**
     * 请求失败的属性
     */
    @SuppressWarnings("all")
    private Integer errcode;
    @SuppressWarnings("all")
    private String errmsg;
}
