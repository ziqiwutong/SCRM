package com.scrm.service.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scrm.service.entity.Order;
import com.scrm.service.service.OrderService;
import com.scrm.service.util.resp.CodeEum;
import com.scrm.service.util.resp.PageResp;
import com.scrm.service.util.resp.Resp;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author Ganyunhui
 * @create 2021-11-03 20:19
 */
@RequestMapping("/order")
@RestController
public class OrderController {
    @Resource
    private OrderService orderService;

    @GetMapping("/query")
    @ResponseBody
    public PageResp query(
            @RequestParam(value = "pageCount", required = false, defaultValue = "10") Integer pageCount,
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(value = "orderStatus", required = false, defaultValue = "") String orderStatus
    ) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        if (!orderStatus.equals("")) {
            wrapper.eq("order_status", Integer.valueOf(orderStatus));
        }
        int total = orderService.queryCount(wrapper);
        return PageResp.success().setData(
                orderService.queryPage(pageCount, currentPage, wrapper)
        ).setPage(pageCount, currentPage, total);
    }

    @GetMapping("/queryByOrderNum")
    @ResponseBody
    public Resp queryById(
            @RequestParam(value = "orderNum") String orderNum
    ) {
        Order order = orderService.queryByOrderNum(orderNum);
        if (null == order) {
            return Resp.error().setMsg("无数据");
        } else {
            return Resp.success().setData(order);
        }
    }

    @PostMapping("/insert")
    @ResponseBody
    public Resp insert(
            @RequestBody Order order
    ) {
        if (order == null) {
            return Resp.error(CodeEum.PARAM_ERROR);
        }
        String result = orderService.insert(order);
        if (result == null) {
            return Resp.success();
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @PostMapping("/update")
    @ResponseBody
    public Resp update(
            @RequestBody Order order
    ) {
        if (order == null) {
            return Resp.error(CodeEum.PARAM_ERROR);
        }
        String result = orderService.update(order);
        if (result == null) {
            return Resp.success();
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public Resp delete(
            @RequestBody HashMap<String, Long> map
    ) {
        if (map == null || !map.containsKey("id")) return Resp.error().setMsg("参数错误");
        Long id = map.get("id");
        if (id == null) return Resp.error().setMsg("参数错误");
        String result = orderService.delete(id);
        if (result == null) {
            return Resp.success();
        } else {
            return Resp.error().setMsg(result);
        }
    }
}
