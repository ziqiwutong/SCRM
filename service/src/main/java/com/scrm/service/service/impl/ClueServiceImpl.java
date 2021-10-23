package com.scrm.service.service.impl;

import com.scrm.service.dao.ClueDao;
import com.scrm.service.entity.Clue;
import com.scrm.service.service.ClueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ClueServiceImpl implements ClueService {
    @Resource
    private ClueDao se_clueDao;

    @Override
    public List<Clue> queryClue(Integer pageCount, Integer currentPage) {
        return se_clueDao.queryClue(pageCount, currentPage);
    }

    @Override
    public List<Clue> queryClueByKey(String key) {
        return se_clueDao.queryClueByKey(key);
    }

    @Override
    public Integer queryCount() {
        return se_clueDao.queryCount();
    }

    @Override
    @Transactional//开启事务
    public String addClue(Clue se_clue) throws Exception{
        int result = se_clueDao.addClue(se_clue);
        if (result < 1) {
            throw new Exception("插入失败");
        }
        return null;
    }

    @Override
    @Transactional//开启事务
    public String editClue(Clue se_clue) throws Exception{
        int result = se_clueDao.editClue(se_clue);
        if (result < 1) {
            throw new Exception("更新失败");
        }
        return null;
    }

    @Override
    @Transactional//开启事务
    public String deleteClue(Integer id) throws Exception{
        int result_clue = se_clueDao.deleteClue(id);
        int result_status = se_clueDao.deleteClueStatus(id);
        if (result_clue < 1 & result_status < 1) {
            throw new Exception("删除失败");
        }
        else if (result_clue < 1 | result_status < 1){
            throw new Exception("索引本身或者索引跟进记录删除失败");
        }
        else {
            return "删除成功";
        }
    }
}
