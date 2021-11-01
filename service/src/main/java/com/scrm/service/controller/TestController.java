package com.scrm.service.controller;

import com.scrm.service.entity.Test;
import com.scrm.service.service.TestService;
import com.scrm.service.util.resp.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RequestMapping("/test")
@RestController
public class TestController {

    @Resource
    private TestService testService;

    @GetMapping("/query")
    @ResponseBody
    public PageResp query(
            @RequestParam(value = "pageCount", required = false, defaultValue = "10") Integer pageCount,
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage
    ) {
//        RestTemplate rest = new RestTemplate();
//        HttpEntity<String> response = rest.exchange("https://mp.weixin.qq.com/s/r3zslmUEY0qUZBO8VO2WtQ", HttpMethod.GET, null, String.class);

        return PageResp.success().setData(
                testService.query(pageCount, currentPage)
        ).setPage(pageCount, currentPage, testService.queryCount()).setMsg("成功");
    }

    @GetMapping("/queryById")
    @ResponseBody
    public Resp queryById(
            @RequestParam(value = "id") Integer id
    ) {
        Test test = testService.queryById(id);
        if (null == test) {
            return Resp.error().setMsg("无数据");
        } else {
            return Resp.success().setData(test).setMsg("成功");
        }
    }

    @PostMapping("/insert")
    @ResponseBody
    public Resp insert(
            @RequestBody Test test
    ) {
        if (test == null) {
            return Resp.error().setMsg("不能为空");
        }
        String result = testService.insert(test);
        if (result == null) {
            return Resp.success().setData(test).setMsg("插入成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @PostMapping("/update")
    @ResponseBody
    public Resp update(
            @RequestBody Test test
    ) {
        if (test == null) {
            return Resp.error().setMsg("不能为空");
        }
        String result = testService.update(test);
        if (result == null) {
            return Resp.success().setData(test).setMsg("更新成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @GetMapping("/delete")
    @ResponseBody
    public Resp delete(
            @RequestParam(value = "id") Integer id
    ) {
        String result = testService.delete(id);
        if (result == null) {
            return Resp.success().setData(id).setMsg("删除成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

}
