package com.scrm.marketing.wx;

import lombok.Data;

import java.util.List;

/**
 * @author fzk
 * @date 2021-11-09 17:17
 */
@Data
public class WxUserInfoResult {
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
    private String headimgurl;
    private List<String> privilege;
    private String unionid;


    /**
     * 错误时微信会返回JSON数据包
     */
    private Integer errcode;
    private String errmsg;

    /**
     * 业务需求附加的属性
     */
    private Integer readTime;// 阅读时长,单位s
    private String readDate;// 阅读日期
    private String readerStatus;// 阅读者状态：不是客户则为null

    private Long articleId;// 文章id
    private Long shareId;// 分享者id
}
