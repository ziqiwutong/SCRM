package com.scrm.service.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scrm.service.entity.Customer;
import com.scrm.service.entity.CustomerCustomizedField;
import com.scrm.service.entity.CustomerRelation;
import com.scrm.service.service.CustomerService;
import com.scrm.service.util.DateTime;
import com.scrm.service.util.VariableName;
import com.scrm.service.util.resp.*;
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
            @RequestParam(value = "asc", required = false, defaultValue = "") String asc,
            @RequestParam(value = "desc", required = false, defaultValue = "") String desc,
            @RequestParam(value = "createTime", required = false, defaultValue = "") String createTime,
            @RequestParam(value = "businessTime", required = false, defaultValue = "") String businessTime,
            @RequestParam HashMap<String, String> map
    ) {
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        for (String key: map.keySet()) {
            if (key.startsWith("eq_")) {
                wrapper.eq(VariableName.camelToUnderscore(key.substring(3)), map.get(key));
            } else if (key.startsWith("in_")) {
                String order = map.get(key);
                wrapper.in(VariableName.camelToUnderscore(key.substring(3)), (Object[])order.split("▓"));
            } else if (key.startsWith("like_")) {
                if (key.startsWith("like_json_")) {
                    wrapper.like("customize_field->'$.\"" + key.substring(10) + "\"'", map.get(key));
                } else {
                    wrapper.like(VariableName.camelToUnderscore(key.substring(5)), map.get(key));
                }
            }
        }
        if (currentPage < 1) currentPage = 1;
        if (pageCount < 1) pageCount = 1;
        if (!asc.isEmpty()) {
            wrapper.orderByAsc(VariableName.camelToUnderscore(asc));
        }
        if (!desc.isEmpty()) {
            wrapper.orderByDesc(VariableName.camelToUnderscore(desc));
        }
        if (!createTime.isEmpty()) {
            if (createTime.equals("为空")) {
                wrapper.isNull("create_time");
            } else {
                String[] range = new String[2];
                DateTime.dateRange(range, createTime);
                if (range[0] != null && range[1] != null) {
                    wrapper.between("create_time", range[0], range[1]);
                } else {
                    return (PageResp) PageResp.error(CodeEum.PARAM_ERROR).setMsg("想peach");
                }
            }
        }
        if (!businessTime.isEmpty()) {
            if (businessTime.equals("为空")) {
                List<Long> ids = customerService.queryIdByBusinessTime(null, null);
                wrapper.notIn("id", ids);
            } else {
                String[] range = new String[2];
                DateTime.dateRange(range, businessTime);
                if (range[0] != null && range[1] != null) {
                    List<Long> ids = customerService.queryIdByBusinessTime(range[0], range[1]);
                    wrapper.in("id", ids);
                } else {
                    return (PageResp) PageResp.error(CodeEum.PARAM_ERROR).setMsg("想peach");
                }
            }
        }
        int total = customerService.queryCount(wrapper);
        return PageResp.success().setData(
                customerService.queryPage(pageCount, currentPage, wrapper)
        ).setPage(pageCount, currentPage, total).setMsg("成功");
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
            return Resp.error(CodeEum.PARAM_ERROR);
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
            return Resp.error(CodeEum.PARAM_ERROR);
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

    @GetMapping("/customizedField")
    @ResponseBody
    public Resp customizedField() {
        List<CustomerCustomizedField> field = customerService.queryCustomizedField();
        if (null == field) {
            return Resp.error().setMsg("无数据");
        } else {
            return Resp.success().setData(field).setMsg("成功");
        }
    }

    @PostMapping("/label")
    @ResponseBody
    public Resp label(
            @RequestParam(value = "id") String id,
            @RequestBody List<Long> labelIds
    ) {
        if (labelIds == null) {
            return Resp.error(CodeEum.PARAM_ERROR);
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
            return Resp.error(CodeEum.PARAM_ERROR);
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
