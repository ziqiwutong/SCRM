package com.scrm.service.service.impl;

import com.scrm.service.dao.ClueStatusDao;
import com.scrm.service.entity.Clue;
import com.scrm.service.entity.ClueStatus;
import com.scrm.service.service.ClueStatusService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ClueStatusServiceImpl implements ClueStatusService {

    @Resource
    private ClueStatusDao se_clue_statusDao;

    @Override
    public List<ClueStatus> queryClueStatus(Integer id) {
        return se_clue_statusDao.queryClueStatus(id);
    }

    @Override
    @Transactional//开启事务
    public String addClueStatus(ClueStatus se_clue_status) throws Exception{
        int result = se_clue_statusDao.addClueStatus(se_clue_status);
        if (result < 1) {
            throw new Exception("插入失败");
        }
        return null;
    }

    @Override
    @Transactional//开启事务
    public String editClueStatus(ClueStatus se_clue_status) throws Exception{
        int result = se_clue_statusDao.editClueStatus(se_clue_status);
        if (result < 1) {
            throw new Exception("更新失败");
        }
        return null;
    }

    @Override
    @Transactional//开启事务
    public String deleteClueStatus(Integer id) throws Exception {
        int result_status = se_clue_statusDao.deleteClueStatus(id);
        if (result_status < 1){
            throw new Exception("索引跟进记录删除失败");
        }
        else {
            return "删除成功";
        }
    }

    @Override
    public Clue queryClue(Integer clue_id) {
        return se_clue_statusDao.queryClue(clue_id);
    }
}

