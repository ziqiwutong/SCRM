package com.scrm.service.service;

import com.scrm.service.entity.Clue;

import java.util.List;

public interface ClueService {
    List<Clue> queryClue(Integer pageCount, Integer currentPage);

    List<Clue> queryClueByKey(String key);

    Integer queryCount();

    String addClue(Clue se_clue);

    String editClue(Clue se_clue);

    String deleteClue(Integer id);
}
