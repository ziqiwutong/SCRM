package com.scrm.service.controller;

import com.scrm.service.entity.Label;
import com.scrm.service.service.LabelService;
import com.scrm.service.util.resp.PageResp;
import com.scrm.service.util.resp.Resp;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping("/label")
@RestController
public class LabelController {

    @Resource
    private LabelService labelService;

    @GetMapping("/query")
    @ResponseBody
    public PageResp query(
            @RequestParam(value = "pageCount", required = false, defaultValue = "10") Integer pageCount,
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage
    ) {
        if (currentPage < 1) currentPage = 1;
        if (pageCount < 1) pageCount = 1;
        return PageResp.success().setData(
                labelService.query(pageCount, currentPage)
        ).setPage(pageCount, currentPage, labelService.queryCount()).setMsg("成功");
    }

    @GetMapping("/queryById")
    @ResponseBody
    public Resp queryById(
            @RequestParam(value = "id") Long id
    ) {
        Label label = labelService.queryById(id);
        if (null == label) {
            return Resp.error().setMsg("无数据");
        } else {
            return Resp.success().setData(label).setMsg("成功");
        }
    }

    @PostMapping("/insert")
    @ResponseBody
    public Resp insert(
            @RequestBody Label label
    ) {
        if (label == null) {
            return Resp.error().setMsg("不能为空");
        }
        String result = labelService.insert(label);
        if (result == null) {
            return Resp.success().setData(label).setMsg("插入成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @PostMapping("/update")
    @ResponseBody
    public Resp update(
            @RequestBody Label label
    ) {
        if (label == null) {
            return Resp.error().setMsg("不能为空");
        }
        String result = labelService.update(label);
        if (result == null) {
            return Resp.success().setData(label).setMsg("更新成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @GetMapping("/delete")
    @ResponseBody
    public Resp delete(
            @RequestParam(value = "id") Long id
    ) {
        String result = labelService.delete(id);
        if (result == null) {
            return Resp.success().setData(id).setMsg("删除成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }
}
