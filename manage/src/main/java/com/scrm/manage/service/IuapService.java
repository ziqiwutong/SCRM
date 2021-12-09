package com.scrm.manage.service;

import com.scrm.manage.share.iuap.UserInfoResult;

/**
 * @author fzk
 * @date 2021-11-18 20:12
 */
public interface IuapService {

    String getAccessToken();

    UserInfoResult getUserInfo(String code);

    Object jsTicket();
}
