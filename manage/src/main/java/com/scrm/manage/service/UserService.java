package com.scrm.manage.service;

import com.scrm.manage.entity.Department;
import com.scrm.manage.entity.UserInfo;
import com.scrm.manage.vo.User;

import java.util.List;

public interface UserService {

    /**
     * 查询User
     * @param count 查询数量
     * @param preId 前一位用户的ID，不传默认为从第一个开始，传入则从传入ID的下一个开始返回
     * @param departmentId 部门ID
     * @return List<User>
     */
    List<User> query(Integer count, String preId, String departmentId);

    /**
     * 查询User
     * @param id User ID
     * @return User
     */
    User queryById(Long id);

    /**
     * 保存附加信息
     * @param userInfo 附加信息
     * @return String
     */
    String save(UserInfo userInfo);

    /**
     * 同步Department
     * @return String
     */
    String syncDepartment();

    /**
     * 查询Department
     * @return List<Department>
     */
    List<Department> queryDepartment();

    /**
     * 懒加载 查询Department
     * @return List<Department>
     */
    List<Department> queryDepartmentLazy(String id);
}
