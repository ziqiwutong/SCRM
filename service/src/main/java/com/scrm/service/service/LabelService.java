package com.scrm.service.service;

import com.scrm.service.entity.Label;

import java.util.List;

public interface LabelService {

    /**
     * 查询Test
     * @param pageCount 每页数量
     * @param currentPage 当前页数
     * @return Label
     */
    List<Label> query(Integer pageCount, Integer currentPage);

    /**
     * 查询Label总数量
     * @return Label数量
     */
    Integer queryCount();

    /**
     * 查询Test
     * @param id Label ID
     * @return Label
     */
    Label queryById(Integer id);

    /**
     * 插入Test
     * @param label Label
     * @return null表示插入成功
     */
    String insert(Label label);

    /**
     * 更新Test
     * @param label Label
     * @return null表示更新成功
     */
    String update(Label label);

    /**
     * 删除Test
     * @param id Label ID
     */
    String delete(Integer id);
}
