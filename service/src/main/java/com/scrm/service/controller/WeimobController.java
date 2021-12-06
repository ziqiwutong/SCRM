package com.scrm.service.controller;

import com.scrm.service.service.WeimobService;
import com.scrm.service.util.resp.Resp;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping("/weimob")
@RestController
public class WeimobController {

    @Resource
    private WeimobService weimobService;

    @GetMapping("/product")
    @ResponseBody
    public Resp product() {
        String result = weimobService.queryProduct();
        if (result == null) {
            return Resp.success().setMsg("同步成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @GetMapping("/order")
    @ResponseBody
    public Resp order() {
        String result = weimobService.queryOrder();
        if (result == null) {
            return Resp.success().setMsg("同步成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }
}
