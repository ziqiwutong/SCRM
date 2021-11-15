package com.scrm.manage.service;

import com.scrm.manage.entity.User;

import java.util.List;

public interface UserService {

    /**
     * 查询User
     * @param pageCount 每页数量
     * @param currentPage 当前页数
     * @return List<User>
     */
    List<User> query(Integer pageCount, Integer currentPage);

    /**
     * 查询User总数量
     * @return User数量
     */
    Integer queryCount();

    /**
     * 查询User
     * @param id User ID
     * @return User
     */
    User queryById(Long id);

    /**
     * 插入User
     * @param user User
     * @return null表示插入成功
     */
    String insert(User user);

    /**
     * 更新User
     * @param user User
     * @return null表示更新成功
     */
    String update(User user);

    /**
     * 删除User
     * @param id User ID
     */
    String delete(Long id);
}
