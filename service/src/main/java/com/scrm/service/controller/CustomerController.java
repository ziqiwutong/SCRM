package com.scrm.service.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scrm.service.entity.Customer;
import com.scrm.service.service.CustomerService;
import com.scrm.service.util.resp.PageResp;
import com.scrm.service.util.resp.Resp;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@RequestMapping("/customer")
@RestController
public class CustomerController {

    @Resource
    private CustomerService customerService;

    @GetMapping("/query")
    @ResponseBody
    public PageResp query(
            @RequestParam(value = "pageCount", required = false, defaultValue = "10") Integer pageCount,
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
            @RequestParam HashMap map
    ) {
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        for (Object o: map.keySet()) {
            if (!(o instanceof String)) {
                continue;
            }
            String key = (String)o;
            if (key.equals("pageCount") || key.equals("currentPage")) {
                continue;
            }
            wrapper.like(key.replaceAll("[A-Z]", "_$0").toLowerCase(), map.get(o));
        }
        if (currentPage < 1) currentPage = 1;
        if (pageCount < 1) pageCount = 1;
        return PageResp.success().setData(
                customerService.query(pageCount, currentPage, wrapper)
        ).setPage(pageCount, currentPage, customerService.queryCount(wrapper)).setMsg("成功");
    }

    @GetMapping("/queryById")
    @ResponseBody
    public Resp queryById(
            @RequestParam(value = "id") Integer id
    ) {
        Customer customer = customerService.queryById(id);
        if (null == customer) {
            return Resp.error().setMsg("无数据");
        } else {
            return Resp.success().setData(customer).setMsg("成功");
        }
    }

    @PostMapping("/insert")
    @ResponseBody
    public Resp insert(
            @RequestBody Customer customer
    ) {
        if (customer == null) {
            return Resp.error().setMsg("不能为空");
        }
        String result = customerService.insert(customer);
        if (result == null) {
            return Resp.success().setData(customer).setMsg("插入成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @PostMapping("/insertBatch")
    @ResponseBody
    public Resp insertBatch(
            @RequestBody List<Customer> customers
    ) {
        String result = customerService.insertBatch(customers);
        if (result == null) {
            return Resp.success().setData(customers).setMsg("插入成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @PostMapping("/update")
    @ResponseBody
    public Resp update(
            @RequestBody Customer customer
    ) {
        if (customer == null) {
            return Resp.error().setMsg("不能为空");
        }
        String result = customerService.update(customer);
        if (result == null) {
            return Resp.success().setData(customer).setMsg("更新成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @GetMapping("/delete")
    @ResponseBody
    public Resp delete(
            @RequestParam(value = "id") Integer id
    ) {
        String result = customerService.delete(id);
        if (result == null) {
            return Resp.success().setData(id).setMsg("删除成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @PostMapping("/deleteBatch")
    @ResponseBody
    public Resp deleteBatch(
            @RequestBody List<Integer> ids
    ) {
        String result = customerService.deleteBatch(ids);
        if (result == null) {
            return Resp.success().setData(ids).setMsg("删除成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }
}
