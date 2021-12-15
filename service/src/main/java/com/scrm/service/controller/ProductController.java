package com.scrm.service.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scrm.service.entity.Product;
import com.scrm.service.service.ProductService;
import com.scrm.service.util.VariableName;
import com.scrm.service.util.resp.CodeEum;
import com.scrm.service.util.resp.PageResp;
import com.scrm.service.util.resp.Resp;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author Ganyunhui
 * @create 2021-10-20 19:17
 */
@RequestMapping("/product")
@RestController
public class ProductController {
    @Resource
    private ProductService productService;

    @GetMapping("/query")
    @ResponseBody
    public PageResp query(
            @RequestParam(value = "pageCount", required = false, defaultValue = "10") Integer pageCount,
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(value = "asc", required = false, defaultValue = "") String asc,
            @RequestParam(value = "desc", required = false, defaultValue = "") String desc,
            @RequestParam HashMap<String, String> map
    ) {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        for (String key : map.keySet()) {
            if (key.startsWith("eq_")) {
                wrapper.eq(VariableName.camelToUnderscore(key.substring(3)), map.get(key));
            } else if (key.startsWith("in_")) {
                String order = map.get(key);
                wrapper.in(VariableName.camelToUnderscore(key.substring(3)), (Object[]) order.split("▓"));
            } else if (key.startsWith("notin_")) {
                String order = map.get(key);
                wrapper.notIn(VariableName.camelToUnderscore(key.substring(6)), (Object[]) order.split("▓"));
            } else if (key.startsWith("bet_")) {
                String[] order = map.get(key).split("▓");
                if (order.length != 2) return PageResp.error().setMsg("参数错误");
                wrapper.between(VariableName.camelToUnderscore(key.substring(4)), order[0], order[1]);
            } else if (key.startsWith("gt_")) {
                wrapper.gt(VariableName.camelToUnderscore(key.substring(3)), map.get(key));
            } else if (key.startsWith("ge_")) {
                wrapper.le(VariableName.camelToUnderscore(key.substring(3)), map.get(key));
            } else if (key.startsWith("lt_")) {
                wrapper.lt(VariableName.camelToUnderscore(key.substring(3)), map.get(key));
            } else if (key.startsWith("le_")) {
                wrapper.le(VariableName.camelToUnderscore(key.substring(3)), map.get(key));
            } else if (key.startsWith("like_")) {
                wrapper.like(VariableName.camelToUnderscore(key.substring(5)), map.get(key));
            }
        }
        if (!asc.isEmpty()) {
            wrapper.orderByAsc(VariableName.camelToUnderscore(asc));
        }
        if (!desc.isEmpty()) {
            wrapper.orderByDesc(VariableName.camelToUnderscore(desc));
        }
        int total = productService.queryCount(wrapper);
        return PageResp.success().setData(
                productService.queryPage(pageCount, currentPage, wrapper)
        ).setPage(pageCount, currentPage, total);
    }

    @GetMapping("/queryById")
    @ResponseBody
    public Resp queryById(
            @RequestParam(value = "id") Long id
    ) {
        Product product = productService.queryById(id);
        if (null == product) {
            return Resp.error().setMsg("无数据");
        } else {
            return Resp.success().setData(product);
        }
    }

    @PostMapping("/insert")
    @ResponseBody
    public Resp insert(
            @RequestBody Product product
    ) {
        if (product == null) {
            return Resp.error(CodeEum.PARAM_ERROR);
        }
        String result = productService.insert(product);
        if (result == null) {
            return Resp.success();
        } else {
            return Resp.error().setMsg(result);
        }
    }

    @PostMapping("/update")
    @ResponseBody
    public Resp update(
            @RequestBody Product product
    ) {
        if (product == null) {
            return Resp.error(CodeEum.PARAM_ERROR);
        }
        String result = productService.update(product);
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
        String result = productService.delete(id);
        if (result == null) {
            return Resp.success();
        } else {
            return Resp.error().setMsg(result);
        }
    }
}
