package com.scrm.service.service.impl;

import com.scrm.service.entity.Test;

import java.util.List;

import com.scrm.service.dao.TestDao;
import com.scrm.service.service.TestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TestServiceImpl implements TestService {

    @Resource
    private TestDao testDao;

    @Override
    public List<Test> query(Integer pageCount, Integer currentPage) {
        return testDao.queryTest(pageCount, currentPage);
    }

    @Override
    public Integer queryCount() {
        return testDao.queryTestCount();
    }

    @Override
    public Test queryById(Integer id) {
        return testDao.queryTestById(id);
    }

    @Override
    public String insert(Test test) {
        int result = testDao.insertTest(test);
        if (result < 1) {
            return "更新失败";
        }
        return null;
    }

    @Override
    public String update(Test test) {
        int result = testDao.updateTest(test);
        if (result < 1) {
            return "更新失败";
        }
        return null;
    }

    @Override
    public String delete(Integer id) {
        int result = testDao.deleteTest(id);
        if (result < 1) {
            return "删除失败";
        }
        return null;
    }
}
