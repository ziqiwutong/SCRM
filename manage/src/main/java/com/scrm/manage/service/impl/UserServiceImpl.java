package com.scrm.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scrm.manage.dao.UserDao;
import com.scrm.manage.entity.User;
import com.scrm.manage.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;
    
    @Override
    public List<User> query(Integer pageCount, Integer currentPage) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        int offset = (currentPage - 1) * pageCount;
        wrapper.last(" limit " + offset + "," + pageCount);
        return userDao.selectList(wrapper);
    }

    @Override
    public Integer queryCount() {
        return userDao.selectCount(null);
    }

    @Override
    public User queryById(Long id) {
        return userDao.selectById(id);
    }

    @Override
    public String insert(User user) {
        int result = userDao.insert(user);
        if (result < 1) {
            return "插入失败";
        }
        return null;
    }

    @Override
    public String update(User user) {
        int result = userDao.updateById(user);
        if (result < 1) {
            return "更新失败";
        }
        return null;
    }

    @Override
    public String delete(Long id) {
        int result = userDao.deleteById(id);
        if (result < 1) {
            return "删除失败";
        }
        return null;
    }
}
