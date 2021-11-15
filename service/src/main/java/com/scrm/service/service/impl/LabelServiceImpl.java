package com.scrm.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scrm.service.dao.LabelDao;
import com.scrm.service.entity.Label;
import com.scrm.service.service.LabelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class LabelServiceImpl implements LabelService {

    @Resource
    private LabelDao labelDao;

    @Override
    public ArrayList<ArrayList<Label>> query(Integer object) {
        QueryWrapper<Label> wrapper = new QueryWrapper<>();
        if (object != -1) {
            wrapper.eq("label_object", object);
        }
        List<Label> list = labelDao.selectList(wrapper);
        HashMap<String, ArrayList<Label>> map = new HashMap<>();
        for (Label label : list) {
            ArrayList<Label> labels = map.getOrDefault(label.getLabelType(), new ArrayList<>());
            labels.add(label);
            map.put(label.getLabelType(), labels);
        }
        ArrayList<ArrayList<Label>> result = new ArrayList<>();
        for (String key : map.keySet()) {
            result.add(map.get(key));
        }
        return result;
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
