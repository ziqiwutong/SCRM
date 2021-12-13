package com.scrm.service.controller;

import com.scrm.service.entity.RelationScore;
import com.scrm.service.service.CustomerRelationSelectService;
import com.scrm.service.util.resp.Result;
import com.scrm.service.vo.FirmRelation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RequestMapping(value = "/relationSelect")
@RestController
public class CustomerRelationSelectController {
    @Resource
    private CustomerRelationSelectService customerRelationSelectService;

    @GetMapping(value = "/queryRelationWithScore")
    public Result queryClue(
            @RequestParam(value = "sourceFirm") String sourceFirm,
            @RequestParam(value = "targetFirm") String targetFirm
    ) {
        ArrayList<ArrayList<FirmRelation>> firmRelations = customerRelationSelectService.queryRelationBetweenFirm(
                sourceFirm, targetFirm);
        List<RelationScore> firmRelationsWithScore = customerRelationSelectService.scoreRelationBetweenFirm(
                firmRelations);

        return Result.success(firmRelationsWithScore);
    }
}
