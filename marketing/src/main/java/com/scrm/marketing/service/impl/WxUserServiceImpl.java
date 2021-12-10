package com.scrm.marketing.service.impl;

import com.scrm.marketing.entity.WxUser;
import com.scrm.marketing.mapper.WxUserMapper;
import com.scrm.marketing.service.WxUserService;
import com.scrm.marketing.share.wx.WxUserInfoResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fzk
 * @date 2021-12-01 21:17
 */
@Service
public class WxUserServiceImpl implements WxUserService {
    @Resource
    private WxUserMapper wxUserMapper;

    @Override
    @Transactional // 开启事务
    public boolean saveWxUser(WxUserInfoResult wxUserInfo) {
        // 0.参数转化
        WxUser wxUser = new WxUser(
                wxUserInfo.getOpenid(),
                wxUserInfo.getNickname(),
                wxUserInfo.getSex(),
                wxUserInfo.getProvince(),
                wxUserInfo.getCity(),
                wxUserInfo.getCountry(),
                wxUserInfo.getHeadimgurl(),
                wxUserInfo.getUnionid(),
                null
        );

        // 1.先判断是否已经存在此WxUser
        List<WxUser> wxUsers =
                wxUserMapper.selectByOpenid(wxUserInfo.getOpenid());

        // 2.没有则插入
        if (wxUsers.size() == 0)
            return 1 == wxUserMapper.insert(wxUser);

            // 3.有则更新: 这里不会更新readerStatus, 仅更新拿到的微信用户信息
        else {
            wxUser.setId(wxUsers.get(0).getId());
            return 1 == wxUserMapper.updateById(wxUser);
        }
    }

    @Override
    @Transactional// 开启事务
    public String bindWxUser(long customerId, String nickname, String openid) {
        // 1.将此微信openid绑定的其他客户都改为未绑定
        wxUserMapper.unBindByOpenid(openid);

        // 2.绑定微信openid到客户表
        if (1 != wxUserMapper.bindWxUser(customerId, nickname, openid))
            return "客户id不存在：" + customerId;

        // 3.将客户状态customer_status字段赋值到微信用户表reader_status字段
        wxUserMapper.copyCusStatusToWxUser(customerId, openid);
        return null;
    }
}
