package com.scrm.marketing.service;

import com.scrm.marketing.share.wx.WxUserInfoResult;

/**
 * @author fzk
 * @date 2021-12-01 21:16
 */
public interface WxUserService {
    boolean saveWxUser(WxUserInfoResult wxUserInfo);
}
