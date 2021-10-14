package com.scrm.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.service.entity.Label;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LabelDao extends BaseMapper<Label> {
}
