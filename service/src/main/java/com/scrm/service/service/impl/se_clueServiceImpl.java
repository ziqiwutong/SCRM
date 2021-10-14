package com.scrm.service.service.impl;

import com.scrm.service.dao.se_clueDao;
import com.scrm.service.entity.se_clue;
import com.scrm.service.service.se_clueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class se_clueServiceImpl implements se_clueService {
    @Resource
    private se_clueDao se_clueDao;

    @Override
    public List<se_clue> queryClue(Integer pageCount, Integer currentPage) {
        return se_clueDao.queryClue(pageCount, currentPage);
    }

    @Override
    public List<se_clue> queryClueByKey(String key) {
        return se_clueDao.queryClueByKey(key);
    }

    @Override
    public Integer queryCount() {
        return se_clueDao.queryCount();
    }

    @Override
    public String addClue(se_clue se_clue) {
        int result = se_clueDao.addClue(se_clue);
        if (result < 1) {
            return "插入失败";
        }
        return null;
    }

    @Override
    public String editClue(se_clue se_clue) {
        int result = se_clueDao.editClue(se_clue);
        if (result < 1) {
            return "更新失败";
        }
        return null;
    }

    @Override
    public String deleteClue(Integer id) {
        int result_clue = se_clueDao.deleteClue(id);
        int result_status = se_clueDao.deleteClueStatus(id);
        if (result_clue < 1 & result_status < 1) {
            return "删除失败";
        }
        else if (result_clue < 1 | result_status < 1){
            return "索引本身或者索引跟进记录删除失败";
        }
        else {
            return "删除成功";
        }
    }
}
