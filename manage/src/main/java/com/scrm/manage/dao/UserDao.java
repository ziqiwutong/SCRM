package com.scrm.manage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.manage.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseMapper<User> {
}
