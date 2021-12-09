package com.scrm.manage.controller;

import com.scrm.manage.entity.UserInfo;
import com.scrm.manage.service.UserService;
import com.scrm.manage.util.resp.*;
import com.scrm.manage.vo.User;
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
            @RequestParam(value = "count", required = false, defaultValue = "10") Integer count,
            @RequestParam(value = "preId", required = false, defaultValue = "") String preId,
            @RequestParam(value = "departmentId", required = false, defaultValue = "") String departmentId
    ) {
        return Resp.success().setData(
                userService.query(count, preId, departmentId)
        );
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

    @PostMapping("/saveAppendInfo")
    @ResponseBody
    public Resp save(
            @RequestBody UserInfo userInfo
    ) {
        if (userInfo == null) {
            return Resp.error().setMsg("不能为空");
        }
        if (userInfo.getId() == null) {
            return Resp.error().setMsg("ID不能为空");
        }
        String result = userService.save(userInfo);
        if (result == null) {
            return Resp.success().setData(userInfo);
        } else {
            return Resp.error().setMsg(result);
        }
    }
}
