package com.scrm.service.service;

import com.scrm.service.entity.se_clue;
import com.scrm.service.entity.se_clue_status;

import java.util.List;

public interface se_clue_statusService {
    List<se_clue_status> queryClueStatus(Integer id);

    String addClueStatus(se_clue_status se_clue_status);

    String editClueStatus(se_clue_status se_clue_status);

    se_clue queryClue(Integer clue_id);
}
