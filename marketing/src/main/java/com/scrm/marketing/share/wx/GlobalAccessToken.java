package com.scrm.marketing.share.wx;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author fzk
 * @date 2021-11-15 20:29
 */
@Data
public class GlobalAccessToken {
    /**
     * 请求成功返回的属性
     */
    private String access_token;
    private Long expires_in;//微信默认是7200s

    /**
     * 请求失败返回的属性
     */
    @JSONField(serialize = false)
    @SuppressWarnings("all")
    private Integer errcode;
    @JSONField(serialize = false)
    @SuppressWarnings("all")
    private String errmsg;

    private long validBefore;//默认应该是得到access_token时间+7000s的时间戳
}
