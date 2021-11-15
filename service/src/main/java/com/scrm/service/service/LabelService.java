package com.scrm.service.service;

import com.scrm.service.entity.Label;

import java.util.ArrayList;

public interface LabelService {

    /**
     * 查询Label
     * @param object 标签对象
     * @return Label
     */
    ArrayList<ArrayList<Label>> query(Integer object);

    /**
     * 查询Label总数量
     * @return Label数量
     */
    Integer queryCount();

    /**
     * 查询Label
     * @param id Label ID
     * @return Label
     */
    Label queryById(Long id);

    /**
     * 插入Label
     * @param label Label
     * @return null表示插入成功
     */
    String insert(Label label);

    /**
     * 更新Label
     * @param label Label
     * @return null表示更新成功
     */
    String update(Label label);

    /**
     * 删除Label
     * @param id Label ID
     */
    String delete(Long id);
}
