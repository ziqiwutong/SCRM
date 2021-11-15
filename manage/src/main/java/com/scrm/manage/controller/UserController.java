package com.scrm.manage.controller;

import com.scrm.manage.entity.User;
import com.scrm.manage.service.UserService;
import com.scrm.manage.util.resp.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping("/user")
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/query")
    @ResponseBody
    public Resp query(
            @RequestParam(value = "pageCount", required = false, defaultValue = "10") Integer pageCount,
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage
    ) {
        if (currentPage < 1) currentPage = 1;
        if (pageCount < 1) pageCount = 1;
        return PageResp.success().setData(
                userService.query(pageCount, currentPage)
        ).setPage(pageCount, currentPage, userService.queryCount());
    }

    @GetMapping("/queryById")
    @ResponseBody
    public Resp queryById(
            @RequestParam(value = "id") Long id
    ) {
        User user = userService.queryById(id);
        if (user == null) {
            return Resp.error().setMsg("无数据");
        } else {
            return Resp.success().setData(user);
        }
    }

    @PostMapping("/insert")
    @ResponseBody
    public Resp insert(
            @RequestBody User user
    ) {
        if (user == null) {
            return Resp.error().setMsg("不能为空");
        }
        String result = userService.insert(user);
        if (result == null) {
            return Resp.success().setData(user).setMsg("插入成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @PostMapping("/update")
    @ResponseBody
    public Resp update(
            @RequestBody User user
    ) {
        if (user == null) {
            return Resp.error().setMsg("不能为空");
        }
        String result = userService.update(user);
        if (result == null) {
            return Resp.success().setData(user).setMsg("更新成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @GetMapping("/delete")
    @ResponseBody
    public Resp delete(
            @RequestParam(value = "id") Long id
    ) {
        String result = userService.delete(id);
        if (result == null) {
            return Resp.success().setData(id).setMsg("删除成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }
}
