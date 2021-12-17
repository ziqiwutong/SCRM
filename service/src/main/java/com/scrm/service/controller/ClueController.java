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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

/**
 * @author Zitong Li
 * @create 2021-11-10 21:57
 */

@RequestMapping(value = "/clue") //Mapping requests
@RestController
public class ClueController {
    @Resource
    private ClueService se_clueService; //Declare service class

    @Resource
    private ClueStatusService se_clue_statusService;

    @GetMapping(value = "/queryClue") //Mapping query requests
    public PageResult queryClue(
            @RequestParam(value = "pageCount", required = false, defaultValue = "10") Integer pageCount,
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(value = "clueList", required = false) String clueList//Get parameters
    ) {
        Integer count = se_clueService.queryCount();
        if(clueList.equals("全部线索")){
            List<Clue> clues = se_clueService.queryClue(pageCount, currentPage, null);
            //If the operation succeeds, the paging query result will be returned
            return PageResult.success(clues, count, currentPage);
        }else{
            List<Clue> clues = se_clueService.queryClue(pageCount, currentPage, clueList);
            //If the operation succeeds, the paging query result will be returned
            return PageResult.success(clues, count, currentPage);
        }
    }

    @GetMapping(value = "/queryClueByKey") //Mapping keyword query requests
    public Result queryClueByKey(
            @RequestParam(value = "keySearch") String keySearch
    ) {
        List<Clue> clue = se_clueService.queryClueByKey(keySearch);

        //If the operation succeeds, the query result will be returned
        return Result.success(clue);
    }

    @PostMapping(value = "/addClue") //Mapping add requests
    public Result addClue(
            @RequestBody Clue se_clue
    ) {
        if (se_clue == null) {

            //If the parameter is null, a param_miss exception will be returned
            return Result.error(CodeEum.PARAM_MISS);
        }
        try{
            se_clueService.addClue(se_clue);
            return Result.success();
        }catch(Exception e){

            //If the operation fails, a fail exception will be returned
            return Result.error(CodeEum.FAIL);
        }
    }

    @PostMapping(value = "/editClue") //Mapping edit requests
    public Result editClue(
            @RequestBody Clue se_clue
    ) {
        if (se_clue == null) {
            return Result.error(CodeEum.PARAM_MISS);
        }
        try {
            if(se_clue.getBizOppFlag() == true){
                BusinessOpportunity businessOpportunity = new BusinessOpportunity();
                businessOpportunity.setBoName(se_clue.getClueName());
                businessOpportunity.setBoStatus("New Business Opportunity");
                businessOpportunity.setBoEditor(se_clue.getClueEditor());
                businessOpportunity.setBoResponsible(se_clue.getClueResponsible());
                se_clueService.editClue(se_clue);
                return Result.success(businessOpportunity);
            }

            se_clueService.editClue(se_clue);
            return Result.success();
        }catch(Exception e) {
            return Result.error(CodeEum.FAIL);
        }
    }

    @PostMapping(value = "/deleteClue") //Mapping delete requests
    public Result deleteClue(
            @RequestParam(value = "id") Integer id
    ) {
        try {
            se_clueService.deleteClue(id);
            return Result.success();
        }catch(Exception e) {
            return Result.error(CodeEum.FAIL);
        }
    }

    @GetMapping(value = "/queryClueStatus")
    public Result queryClueStatus(
            @RequestParam(value = "clueId") Integer clueId
    ) {
        Clue se_clue = se_clue_statusService.queryClue(clueId);
        List<ClueStatus> list = se_clue_statusService.queryClueStatus(clueId);

        List final_list = new ArrayList();
        final_list.add(se_clue);
        for (ClueStatus clueStatus:list) {
            final_list.add(clueStatus);
        }
        return Result.success(final_list);
    }

    @GetMapping(value = "/queryClueEditStatus")
    public Result queryClueEditStatus(
            @RequestParam(value = "id") Integer id
    )
    {
        ClueStatus clueStatus = se_clue_statusService.queryClueEditStatus(id);
        return Result.success(clueStatus);
    }

    @PostMapping(value = "/addClueStatus")
    public Result addClueStatus(
            @RequestBody ClueStatus se_clue_status
    )
    {
        if (se_clue_status == null || se_clue_status.getClueId() == null) {
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
            @RequestBody ClueStatus se_clue_status
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

    @PostMapping(value = "/deleteClueStatus")
    public Result deleteClueStatus(
            @RequestParam(value = "id") Integer id
    )
    {
        try {
            se_clue_statusService.deleteClueStatus(id);
            return Result.success();
        }catch(Exception e) {
            return Result.error(CodeEum.FAIL);
        }
    }
}
