package com.scrm.marketing.service;

import com.scrm.marketing.share.wx.WxUserInfoResult;

/**
 * @author fzk
 * @date 2021-12-01 21:16
 */
public interface WxUserService {
    boolean saveWxUser(WxUserInfoResult wxUserInfo);

    /**
     * 1.将此微信openid绑定的其他客户都改为未绑定
     * 2.绑定微信用户到客户
     * 3.将客户customer_status字段值复制到微信用户reader_status字段
     *
     * @param customerId 客户id
     * @param nickname   微信昵称
     * @param openid     微信openid
     * @return 返回null表示操作成功
     */
    String bindWxUser(long customerId, String nickname, String openid);
}
