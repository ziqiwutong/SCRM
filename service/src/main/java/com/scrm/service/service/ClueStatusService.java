package com.scrm.service.service;

import com.scrm.service.entity.Clue;
import com.scrm.service.entity.ClueStatus;

import java.util.List;

public interface ClueStatusService {
    List<ClueStatus> queryClueStatus(Integer id);

    String addClueStatus(ClueStatus se_clue_status) throws Exception;

    String editClueStatus(ClueStatus se_clue_status) throws Exception;

    Clue queryClue(Integer clue_id);
}
