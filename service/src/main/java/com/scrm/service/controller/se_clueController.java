package com.scrm.service.controller;

import com.scrm.service.entity.se_clue;
import com.scrm.service.entity.se_clue_status;
import com.scrm.service.service.se_clueService;
import com.scrm.service.service.se_clue_statusService;
import com.scrm.service.util.resp.PageResp;
import com.scrm.service.util.resp.Resp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RequestMapping(value="/clue")
@Controller
public class se_clueController {
    @Resource
    private se_clueService se_clueService;

    @Resource
    private se_clue_statusService se_clue_statusService;

    @RequestMapping(value="/queryClue")
    @ResponseBody
    public PageResp queryClue(
            @RequestParam(value = "pageCount", required = false) Integer pageCount,
            @RequestParam(value = "currentPage", required = false) Integer currentPage)
    {
        return PageResp.success().setData(
                se_clueService.queryClue(pageCount, pageCount)
        ).setPage(pageCount, currentPage, se_clueService.queryCount()).setMsg("成功");
    }

    @RequestMapping(value="/queryClueByKey")
    @ResponseBody
    public Resp queryClueByKey(
            @RequestParam(value = "key") String key
    )
    {
        List<se_clue> se_clue = se_clueService.queryClueByKey(key);
        if (se_clue.size() == 0) {
            return Resp.error().setMsg("无数据");
        } else {
            return Resp.success().setData(se_clue).setMsg("成功");
        }
    }

    @RequestMapping(value="/addClue")
    @ResponseBody
    public Resp addClue(
            @RequestBody se_clue se_clue
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

    @RequestMapping(value="/editClue")
    @ResponseBody
    public Resp editClue(
            @RequestBody se_clue se_clue
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

    @RequestMapping(value="/deleteClue")
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

    @RequestMapping(value="/queryClueStatus")
    @ResponseBody
    public Resp queryClueStatus(
            @RequestParam(value = "id") Integer id
    )
    {
        se_clue se_clue = se_clue_statusService.queryClue(id);
        List<se_clue_status> list = se_clue_statusService.queryClueStatus(id);
        List final_list = new ArrayList();
        final_list.add(se_clue);
        final_list.add(list);
        return Resp.success().setData(
                final_list
        ).setMsg("成功");
    }

    @RequestMapping(value="/addClueStatus")
    @ResponseBody
    public Resp addClueStatus(
            @RequestBody se_clue_status se_clue_status
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

    @RequestMapping(value="/editClueStatus")
    @ResponseBody
    public Resp editClueStatus(
            @RequestBody se_clue_status se_clue_status
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
