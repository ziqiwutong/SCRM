package com.scrm.service.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scrm.service.entity.Customer;
import com.scrm.service.entity.CustomerRelation;
import com.scrm.service.service.CustomerService;
import com.scrm.service.util.resp.PageResp;
import com.scrm.service.util.resp.Resp;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

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
            if (key.equals("asc")) {
                Object order = map.get(o);
                if (!(order instanceof String)) {
                    continue;
                }
                String column = (String)order;
                wrapper.orderByAsc(camelToUnderscore(column));
                continue;
            }
            if (key.equals("desc")) {
                Object order = map.get(o);
                if (!(order instanceof String)) {
                    continue;
                }
                String column = (String)order;
                wrapper.orderByDesc(camelToUnderscore(column));
                continue;
            }
            if (key.startsWith("eq_")) {
                wrapper.eq(camelToUnderscore(key.substring(3)), map.get(o));
            } else if (key.startsWith("in_")) {
                Object order = map.get(o);
                if (!(order instanceof String)) {
                    continue;
                }
                String column = (String)order;
                wrapper.in(camelToUnderscore(key.substring(3)), (Object[]) column.split("▓"));
            } else if (key.startsWith("like_")) {
                wrapper.like(camelToUnderscore(key.substring(5)), map.get(o));
            }
        }
        if (currentPage < 1) currentPage = 1;
        if (pageCount < 1) pageCount = 1;
        int total = customerService.queryCount(wrapper);
        return PageResp.success().setData(
                customerService.query(pageCount, currentPage, wrapper)
        ).setPage(pageCount, currentPage, total).setMsg("成功");
    }

    private String camelToUnderscore(String camel) {
        return camel.replaceAll("[A-Z]", "_$0").toLowerCase();
    }

    @GetMapping("/queryById")
    @ResponseBody
    public Resp queryById(
            @RequestParam(value = "id") Long id
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
            @RequestParam(value = "id") Long id
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
            @RequestBody List<Long> ids
    ) {
        String result = customerService.deleteBatch(ids);
        if (result == null) {
            return Resp.success().setData(ids).setMsg("删除成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @PostMapping("/label")
    @ResponseBody
    public Resp label(
            @RequestParam(value = "id") String id,
            @RequestBody List<Long> labelIds
    ) {
        if (labelIds == null) {
            return Resp.error().setMsg("不能为空");
        }
        String[] ids = id.split(",");
        HashSet<Long> customerIds = new HashSet<>();
        for (String item: ids) {
            customerIds.add(Long.valueOf(item));
        }
        String result = customerService.label(customerIds, labelIds);
        if (result == null) {
            return Resp.success().setData(null).setMsg("更新成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @PostMapping("/relation/insert")
    @ResponseBody
    public Resp insertRelation(
            @RequestBody CustomerRelation relation
    ) {
        if (relation == null) {
            return Resp.error().setMsg("不能为空");
        }
        String result = customerService.insertRelation(relation);
        if (result == null) {
            return Resp.success().setData(relation).setMsg("插入成功");
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @GetMapping("/relation/query")
    @ResponseBody
    public Resp queryRelationById(
            @RequestParam(value = "id") Long id
    ) {
        List<CustomerRelation> relation = customerService.queryRelationById(id);
        if (relation != null) {
            return Resp.success().setData(relation);
        } else {
            return Resp.error().setMsg("获取失败");
        }
    }
}
