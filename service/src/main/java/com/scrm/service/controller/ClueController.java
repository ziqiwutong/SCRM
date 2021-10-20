package com.scrm.service.controller;

import com.scrm.service.entity.Clue;
import com.scrm.service.entity.ClueStatus;
import com.scrm.service.service.ClueService;
import com.scrm.service.service.ClueStatusService;
import com.scrm.service.util.resp.CodeEum;
import com.scrm.service.util.resp.PageResult;
import com.scrm.service.util.resp.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RequestMapping(value = "/clue")
@RestController
public class ClueController {
    @Resource
    private ClueService se_clueService;

    @Resource
    private ClueStatusService se_clue_statusService;

    @GetMapping(value = "/queryClue")
    @ResponseBody
    public PageResult queryClue(
            @RequestParam(value = "pageCount", required = false) Integer pageCount,
            @RequestParam(value = "currentPage", required = false) Integer currentPage) {
        List<Clue> clues = se_clueService.queryClue(pageCount, pageCount);
        Integer count = se_clueService.queryCount();
        return PageResult.success(clues, count, currentPage);
    }

    @GetMapping(value = "/queryClueByKey")
    @ResponseBody
    public Result queryClueByKey(
            @RequestParam(value = "key") String key
    ) {
        List<Clue> se_clue = se_clueService.queryClueByKey(key);
        return Result.success(se_clue);
    }

    @PostMapping(value = "/addClue")
    @ResponseBody
    public Result addClue(
            @RequestBody Clue se_clue
    ) {
        if (se_clue == null) {
            return Result.error(CodeEum.PARAM_MISS);
        }
        String result = se_clueService.addClue(se_clue);
        if (result == null) {
            return Resp.success().setMsg("插入成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @PostMapping(value = "/editClue")
    @ResponseBody
    public Resp editClue(
            @RequestBody Clue se_clue
    ) {
        if (se_clue == null) {
            return Resp.error().setMsg("不能为空");
        }
        String result = se_clueService.editClue(se_clue);
        if (result == null) {
            return Resp.success().setMsg("更新成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @PostMapping(value = "/deleteClue")
    @ResponseBody
    public Resp deleteClue(
            @RequestParam(value = "id") Integer id
    ) {
        String result = se_clueService.deleteClue(id);
        if (result == null) {
            return Resp.success().setMsg("删除成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @GetMapping(value = "/queryClueStatus")
    @ResponseBody
    public Resp queryClueStatus(
            @RequestParam(value = "id") Integer id
    ) {
        Clue se_clue = se_clue_statusService.queryClue(id);
        List<ClueStatus> list = se_clue_statusService.queryClueStatus(id);
        List final_list = new ArrayList();
        final_list.add(se_clue);
        final_list.add(list);
        return Resp.success().setData(
                final_list
        ).setMsg("成功");
    }

    @PostMapping(value = "/addClueStatus")
    @ResponseBody
    public Resp addClueStatus(
            @RequestBody ClueStatus se_clue_status
    ) {
        if (se_clue_status == null) {
            return Resp.error().setMsg("不能为空");
        }
        String result = se_clue_statusService.addClueStatus(se_clue_status);
        if (result == null) {
            return Resp.success().setMsg("插入成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @PostMapping(value = "/editClueStatus")
    @ResponseBody
    public Resp editClueStatus(
            @RequestBody ClueStatus se_clue_status
    ) {
        if (se_clue_status == null) {
            return Resp.error().setMsg("不能为空");
        }
        String result = se_clue_statusService.editClueStatus(se_clue_status);
        if (result == null) {
            return Resp.success().setMsg("更新成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }
}
