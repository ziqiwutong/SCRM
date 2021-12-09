package com.scrm.manage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.manage.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoDao extends BaseMapper<UserInfo> {
}
