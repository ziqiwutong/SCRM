package com.scrm.marketing.wx;

import lombok.Data;

import java.util.List;

/**
 * @author fzk
 * @date 2021-11-09 17:17
 */
@Data
public class UserInfoResult {
    /**
     * 正确时返回的JSON数据包如下
     */
    private String openid;
    private String nickname;
    /*用户的性别，值为1时是男性，值为2时是女性，值为0时是未知*/
    private String sex;
    private String province;
    private String city;
    private String country;
    private String headimgUrl;
    private List<String> privilege;
    private String unionid;


    /**
     * 错误时微信会返回JSON数据包
     */
    private Integer errcode;
    private String errmsg;
}
