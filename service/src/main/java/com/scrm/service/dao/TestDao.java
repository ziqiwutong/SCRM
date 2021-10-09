package com.scrm.service.dao;

import com.scrm.service.entity.Test;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TestDao{
    /**
     * 分页查询
     * @param pageCount 一页的数量，可为null
     * @param currentPage 当前页码，从1开始，可为null
     * @return Test集合
     */
    List<Test> queryTest(Integer pageCount, Integer currentPage);

    /**
     * 查询Test总数量
     * @return Test数量
     */
    Integer queryTestCount();

    /**
     * 根据ID查询Test
     * @param id Test ID
     * @return Test
     */
    Test queryTestById(Integer id);

    /**
     * 插入Test
     */
    Integer insertTest(Test test);

    /**
     * 更新Test
     */
    Integer updateTest(Test test);

    /**
     * 删除Test
     * @param id Test ID
     */
    Integer deleteTest(Integer id);
}
