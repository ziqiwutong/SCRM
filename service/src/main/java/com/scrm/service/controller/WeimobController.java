package com.scrm.service.controller;

import com.scrm.service.service.WeimobService;
import com.scrm.service.util.resp.PageResp;
import com.scrm.service.util.resp.Resp;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping("/weimob")
@RestController
public class WeimobController {

    @Resource
    private WeimobService weimobService;

    @GetMapping("/queryProduct")
    @ResponseBody
    public Resp queryProduct(
            @RequestParam(value = "pageCount", required = false, defaultValue = "10") Integer pageCount,
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage
    ) {
        return PageResp.success().setData(
                weimobService.queryProduct(pageCount, currentPage)
        ).setPage(pageCount, currentPage, weimobService.queryProductCount());
    }

    @GetMapping("/product")
    @ResponseBody
    public Resp product() {
        String result = weimobService.syncProduct();
        if (result == null) {
            return Resp.success().setMsg("同步成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @GetMapping("/order")
    @ResponseBody
    public Resp order() {
        String result = weimobService.syncOrder();
        if (result == null) {
            return Resp.success().setMsg("同步成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @GetMapping("/distributeUrl")
    @ResponseBody
    public Resp distributeUrl(
            @RequestParam(value = "id") Long userId
    ) {
        String result = weimobService.distributeUrl(userId);
        if (result != null) {
            return Resp.success().setData(result);
        } else {
            return Resp.error();
        }
    }
}
