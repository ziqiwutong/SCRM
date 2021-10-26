package com.scrm.service.controller;

import com.scrm.service.entity.BusinessOpportunity;
import com.scrm.service.entity.Clue;
import com.scrm.service.entity.ClueStatus;
import com.scrm.service.service.ClueService;
import com.scrm.service.service.ClueStatusService;
import com.scrm.service.util.resp.CodeEum;
import com.scrm.service.util.resp.PageResult;
import com.scrm.service.util.resp.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RequestMapping(value = "/clue")
@RestController
public class ClueController {
    @Resource
    private ClueService se_clueService;

    @Resource
    private ClueStatusService se_clue_statusService;

    @PostMapping(value = "/queryClue")
    public PageResult queryClue(
            @RequestParam(value = "pageCount", required = false) Integer pageCount,
            @RequestParam(value = "currentPage", required = false) Integer currentPage) {
        List<Clue> clues = se_clueService.queryClue(pageCount, pageCount);
        Integer count = se_clueService.queryCount();
        return PageResult.success(clues, count, currentPage);
    }

    @PostMapping(value = "/queryClueByKey")
    public Result queryClueByKey(
            @RequestParam(value = "keySearch") String keySearch
    ) {
        List<Clue> clue = se_clueService.queryClueByKey(keySearch);
        return Result.success(clue);
    }

    @PostMapping(value = "/addClue")
    public Result addClue(
            Clue se_clue
    ) {
        if (se_clue == null) {
            return Result.error(CodeEum.PARAM_MISS);
        }
        try{
            se_clueService.addClue(se_clue);
            return Result.success();
        }catch(Exception e){
            return Result.error(CodeEum.FAIL);
        }
    }

    @PostMapping(value = "/editClue")
    public Result editClue(
            Clue se_clue
    )
    {
        if (se_clue == null) {
            return Result.error(CodeEum.PARAM_MISS);
        }
        try {
            if(se_clue.getBusinessOpporitunityFlag() == true){
                BusinessOpportunity businessOpportunity = new BusinessOpportunity();
                businessOpportunity.setBoName(se_clue.getClueName());
                businessOpportunity.setBoDate(new Date(System.currentTimeMillis()));
                businessOpportunity.setBoStatus("新商机");
                businessOpportunity.setBoEditor(se_clue.getClueEditor());
                businessOpportunity.setBoResponsible(se_clue.getClueResponsible());
                return Result.success(businessOpportunity);
            }

            se_clueService.editClue(se_clue);
            return Result.success();
        }catch(Exception e) {
            return Result.error(CodeEum.FAIL);
        }
    }

    @PostMapping(value = "/deleteClue")
    public Result deleteClue(
            @RequestParam(value = "id") Integer id
    )
    {
        try {
            se_clueService.deleteClue(id);
            return Result.success();
        }catch(Exception e) {
            return Result.error(CodeEum.FAIL);
        }
    }

    @PostMapping(value = "/queryClueStatus")
    public Result queryClueStatus(
            @RequestParam(value = "clueId") Integer clueId
    )
    {
        Clue se_clue = se_clue_statusService.queryClue(clueId);
        List<ClueStatus> list = se_clue_statusService.queryClueStatus(clueId);
        List final_list = new ArrayList();
        final_list.add(se_clue);
        for (ClueStatus clueStatus:list
             ) {
            final_list.add(clueStatus);
        }
        return Result.success(final_list);
    }

    @PostMapping(value = "/addClueStatus")
    public Result addClueStatus(
            ClueStatus se_clue_status
    )
    {
        if (se_clue_status == null) {
            return Result.error(CodeEum.PARAM_MISS);
        }
        try {
            se_clue_statusService.addClueStatus(se_clue_status);
            return Result.success();
        }catch(Exception e) {
            return Result.error(CodeEum.FAIL);
        }
    }

    @PostMapping(value = "/editClueStatus")
    public Result editClueStatus(
            ClueStatus se_clue_status
    )
    {
        if (se_clue_status == null) {
            Result.error(CodeEum.PARAM_MISS);
        }
        try {
            se_clue_statusService.editClueStatus(se_clue_status);
            return Result.success();
        }catch(Exception e) {
            return Result.error(CodeEum.FAIL);
        }
    }
}
