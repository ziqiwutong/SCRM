package com.scrm.marketing.wx;

import lombok.Data;

/**
 * @author fzk
 * @date 2021-11-09 17:05
 */
@Data
public class AccessTokenResult {
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
    private Integer errcode;
    private String errmsg;
}
