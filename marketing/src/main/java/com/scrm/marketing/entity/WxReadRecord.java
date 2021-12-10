package com.scrm.marketing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 保存微信用户每日文章阅读记录
 * <p>
 * 如果一天内用户多次阅读，算多条记录，不合并阅读时长
 *
 * @author fzk
 * @date 2021-11-28 14:00
 */
@Data
@TableName("mk_wx_read_record")
public class WxReadRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 关联表相关id属性
     */
    private Long articleId;
    private String shareId;
    private Long wid;
    private String openid;
    /**
     * 记录属性
     */
    private String readDate;
    private Integer readTime;//单位s

    /**
     * 表修改记录属性
     */
    private String createTime;
    private String updateTime;

    /*构造器*/
    public WxReadRecord() {
    }

    public WxReadRecord(Long articleId, String shareId, Long wid, String openid, String readDate, Integer readTime) {
        this.articleId = articleId;
        this.shareId = shareId;
        this.wid = wid;
        this.openid = openid;
        this.readDate = readDate;
        this.readTime = readTime;
    }

    /*用于前端展示的相关属性*/
    @TableField(exist = false)
    private String nickname;
    @TableField(exist = false)
    private String sex; // 用户的性别，1是男性，2是女性，0是未知
    @TableField(exist = false)
    private String province;
    @TableField(exist = false)
    private String city;
    @TableField(exist = false)
    private String country;
    @TableField(exist = false)
    private String headimgurl;
    @TableField(exist = false)
    private String unionid;

    @TableField(exist = false)
    private String readerStatus;// 读者状态：如果是客户，则为客户状态，否则为null

    /**
     * 用于快速将 WxUser中的信息填充到WxReadRecord中
     *
     * @param wxReadRecord 被填充的对象
     * @param wxUser       信息来源
     */
    public static void fastFillField(WxReadRecord wxReadRecord, WxUser wxUser) {
        wxReadRecord.setNickname(wxUser.getNickname());
        wxReadRecord.setSex(wxUser.getSex());
        wxReadRecord.setProvince(wxUser.getProvince());
        wxReadRecord.setCity(wxUser.getCity());
        wxReadRecord.setCountry(wxUser.getCountry());
        wxReadRecord.setHeadimgurl(wxUser.getHeadimgurl());
        wxReadRecord.setUnionid(wxUser.getUnionid());
        wxReadRecord.setReaderStatus(wxUser.getReaderStatus());
    }
}
