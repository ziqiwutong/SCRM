package com.scrm.service.controller;

import com.scrm.service.entity.Clue;
import com.scrm.service.entity.ClueStatus;
import com.scrm.service.service.ClueService;
import com.scrm.service.service.ClueStatusService;
import com.scrm.service.util.resp.PageResp;
import com.scrm.service.util.resp.Resp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RequestMapping(value="/clue")
@RestController
public class ClueController {
    @Resource
    private ClueService se_clueService;

    @Resource
    private ClueStatusService se_clue_statusService;

    @GetMapping(value="/queryClue")
    @ResponseBody
    public PageResp queryClue(
            @RequestParam(value = "pageCount", required = false) Integer pageCount,
            @RequestParam(value = "currentPage", required = false) Integer currentPage)
    {
        return PageResp.success().setData(
                se_clueService.queryClue(pageCount, pageCount)
        ).setPage(pageCount, currentPage, se_clueService.queryCount()).setMsg("成功");
    }

    @GetMapping(value="/queryClueByKey")
    @ResponseBody
    public Resp queryClueByKey(
            @RequestParam(value = "key") String key
    )
    {
        List<Clue> se_clue = se_clueService.queryClueByKey(key);
        if (se_clue.size() == 0) {
            return Resp.error().setMsg("无数据");
        } else {
            return Resp.success().setData(se_clue).setMsg("成功");
        }
    }

    @PostMapping(value="/addClue")
    @ResponseBody
    public Resp addClue(
            @RequestBody Clue se_clue
    )
    {
        if (se_clue == null) {
            return Resp.error().setMsg("不能为空");
        }
        String result = se_clueService.addClue(se_clue);
        if (result == null) {
            return Resp.success().setMsg("插入成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @PostMapping(value="/editClue")
    @ResponseBody
    public Resp editClue(
            @RequestBody Clue se_clue
    )
    {
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

    @PostMapping(value="/deleteClue")
    @ResponseBody
    public Resp deleteClue(
            @RequestParam(value = "id") Integer id
    )
    {
        String result = se_clueService.deleteClue(id);
        if (result == null) {
            return Resp.success().setMsg("删除成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @GetMapping(value="/queryClueStatus")
    @ResponseBody
    public Resp queryClueStatus(
            @RequestParam(value = "id") Integer id
    )
    {
        Clue se_clue = se_clue_statusService.queryClue(id);
        List<ClueStatus> list = se_clue_statusService.queryClueStatus(id);
        List final_list = new ArrayList();
        final_list.add(se_clue);
        final_list.add(list);
        return Resp.success().setData(
                final_list
        ).setMsg("成功");
    }

    @PostMapping(value="/addClueStatus")
    @ResponseBody
    public Resp addClueStatus(
            @RequestBody ClueStatus se_clue_status
    )
    {
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

    @PostMapping(value="/editClueStatus")
    @ResponseBody
    public Resp editClueStatus(
            @RequestBody ClueStatus se_clue_status
    )
    {
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
