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
            wxUserMapper.insert(wxUser);

        // 3.有则更新: 这里不会更新readerStatus, 仅更新拿到的微信用户信息
        wxUser.setId(wxUsers.get(0).getId());
        wxUserMapper.updateById(wxUser);

        return true;
    }
}
