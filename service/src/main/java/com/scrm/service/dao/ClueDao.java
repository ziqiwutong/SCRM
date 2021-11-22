package com.scrm.service.dao;

import com.scrm.service.entity.Clue;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClueDao {
    List<Clue> queryClue(Integer pageCount, Integer currentPage, String clueList);

    List<Clue> queryClueByKey(String key);

    Integer queryCount();

    Integer addClue(Clue se_clue);

    Integer editClue(Clue se_clue);

    Integer deleteClue(Integer id);

    Integer deleteClueStatus(Integer id);
}

