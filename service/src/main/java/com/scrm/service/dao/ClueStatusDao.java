package com.scrm.service.dao;

import com.scrm.service.entity.Clue;
import com.scrm.service.entity.ClueStatus;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClueStatusDao {
    List<ClueStatus> queryClueStatus(Integer id);

    ClueStatus queryClueEditStatus(Integer id);

    Integer addClueStatus(ClueStatus se_clue_status);

    Integer editClueStatus(ClueStatus se_clue_status);

    Integer deleteClueStatus(Integer id);

    Clue queryClue(Integer clue_id);
}
