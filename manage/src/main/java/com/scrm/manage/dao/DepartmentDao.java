package com.scrm.manage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.manage.entity.Department;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DepartmentDao extends BaseMapper<Department> {
}
