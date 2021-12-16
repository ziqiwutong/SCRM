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
        if (result != null) {
            return Resp.success().setData(result);
        } else {
            return Resp.error();
        }
    }

    @GetMapping("/syncDepartment")
    @ResponseBody
    public Resp syncDepartment() {
        String result = userService.syncDepartment();
        if (result == null) {
            return Resp.success();
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @GetMapping("/department")
    @ResponseBody
    public Resp queryDepartment() {
        return Resp.success().setData(userService.queryDepartment());
    }

    @GetMapping("/departmentLazy")
    @ResponseBody
    public Resp queryDepartmentLazy(
            @RequestParam(value = "id", required = false, defaultValue = "") String id
    ) {
        return Resp.success().setData(userService.queryDepartmentLazy(id));
    }
}
