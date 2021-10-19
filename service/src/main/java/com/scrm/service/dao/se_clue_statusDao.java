package com.scrm.service.dao;

import com.scrm.service.entity.se_clue;
import com.scrm.service.entity.se_clue_status;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface se_clue_statusDao {
    List<se_clue_status> queryClueStatus(Integer id);

    Integer addClueStatus(se_clue_status se_clue_status);

    Integer editClueStatus(se_clue_status se_clue_status);

    se_clue queryClue(Integer clue_id);
}
