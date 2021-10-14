package com.scrm.service.service;

import com.scrm.service.entity.se_clue;

import java.util.List;

public interface se_clueService {
    List<se_clue> queryClue(Integer pageCount, Integer currentPage);

    List<se_clue> queryClueByKey(String key);

    Integer queryCount();

    String addClue(se_clue se_clue);

    String editClue(se_clue se_clue);

    String deleteClue(Integer id);
}
