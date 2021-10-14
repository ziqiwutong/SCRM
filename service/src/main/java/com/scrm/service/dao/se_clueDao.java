package com.scrm.service.dao;

import com.scrm.service.entity.se_clue;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface se_clueDao {
    List<se_clue> queryClue(Integer pageCount, Integer currentPage);

    List<se_clue> queryClueByKey(String key);

    Integer queryCount();

    Integer addClue(se_clue se_clue);

    Integer editClue(se_clue se_clue);

    Integer deleteClue(Integer id);

    Integer deleteClueStatus(Integer id);
}

