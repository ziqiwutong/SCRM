package com.scrm.marketing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author fzk
 * @date 2021-11-13 17:29
 * 保存微信用户信息
 */
@Data
@TableName("mk_wx_user")
public class WxUser {
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 微信用户相关属性
     */
    private String openid;
    private String nickname;
    private String sex; // 用户的性别，1是男性，2是女性，0是未知
    private String province;
    private String city;
    private String country;
    private String headimgurl;
    //private List<String> privilege;
    private String unionid;

    private String readerStatus;// 读者状态：如果是客户，则为客户状态，否则为null

    /**
     * 表修改记录属性
     */
    private String createTime;
    private String updateTime;

    /*构造器*/
    public WxUser() {
    }

    public WxUser(String openid, String nickname, String sex, String province, String city, String country, String headimgurl, String unionid,String readerStatus) {
        this.openid = openid;
        this.nickname = nickname;
        this.sex = sex;
        this.province = province;
        this.city = city;
        this.country = country;
        this.headimgurl = headimgurl;
        this.unionid = unionid;
        this.readerStatus=readerStatus;
    }
}
