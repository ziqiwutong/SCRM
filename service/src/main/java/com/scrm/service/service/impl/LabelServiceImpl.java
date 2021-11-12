package com.scrm.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scrm.service.dao.LabelDao;
import com.scrm.service.entity.Label;
import com.scrm.service.service.LabelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LabelServiceImpl implements LabelService {

    @Resource
    private LabelDao labelDao;

    @Override
    public List<Label> query(Integer pageCount, Integer currentPage) {
        QueryWrapper<Label> wrapper = new QueryWrapper<>();
        int offset = (currentPage - 1) * pageCount;
        wrapper.last(" limit " + offset + "," + pageCount);
        return labelDao.selectList(wrapper);
    }

    @Override
    public Integer queryCount() {
        return labelDao.selectCount(null);
    }

    @Override
    public Label queryById(Long id) {
        return labelDao.selectById(id);
    }

    @Override
    public String insert(Label label) {
        int result = labelDao.insert(label);
        if (result < 1) {
            return "更新失败";
        }
        return null;
    }

    @Override
    public String update(Label label) {
        int result = labelDao.updateById(label);
        if (result < 1) {
            return "更新失败";
        }
        return null;
    }

    @Override
    public String delete(Long id) {
        int result = labelDao.deleteById(id);
        if (result < 1) {
            return "删除失败";
        }
        return null;
    }
}
