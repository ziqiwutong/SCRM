package com.scrm.service.controller;

import com.scrm.service.dao.LabelDao;
import com.scrm.service.entity.Label;
import com.scrm.service.service.LabelService;
import com.scrm.service.util.resp.Resp;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping("/label")
@RestController
public class LabelController {

    @Resource
    private LabelService labelService;

    @Resource
    private LabelDao labelDao;

    @GetMapping("/queryById")
    @ResponseBody
    public Resp queryById(
            @RequestParam(value = "id") Integer id
    ) {
        Label label = labelDao.selectById(id);
        if (null == label) {
            return Resp.error().setMsg("无数据");
        } else {
            return Resp.success().setData(label).setMsg("成功");
        }
    }
}
