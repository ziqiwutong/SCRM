package com.scrm.service.service;

import com.scrm.service.entity.Test;

import java.util.List;

public interface TestService {

    /**
     * 查询Test
     * @param pageCount 每页数量
     * @param currentPage 当前页数
     * @return Test
     */
    List<Test> query(Integer pageCount, Integer currentPage);

    /**
     * 查询Test总数量
     * @return Test数量
     */
    Integer queryCount();

    /**
     * 查询Test
     * @param id Test ID
     * @return Test
     */
    Test queryById(Integer id);

    /**
     * 插入Test
     * @param test Test
     * @return null表示插入成功
     */
    String insert(Test test);

    /**
     * 更新Test
     * @param test Test
     * @return null表示更新成功
     */
    String update(Test test);

    /**
     * 删除Test
     * @param id Test ID
     */
    String delete(Integer id);
}
