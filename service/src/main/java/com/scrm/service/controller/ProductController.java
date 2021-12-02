package com.scrm.service.controller;

import com.alibaba.fastjson.JSON;
import com.scrm.service.entity.OrderAndOrderDetail;
import com.scrm.service.entity.Product;
import com.scrm.service.service.ProductService;
import com.scrm.service.util.resp.CodeEum;
import com.scrm.service.util.resp.PageResp;
import com.scrm.service.util.resp.Resp;
import com.scrm.service.util.resp.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Ganyunhui
 * @create 2021-10-20 19:17
 */
@Slf4j
@RequestMapping("/product")
@RestController
public class ProductController {
    @Resource
    private ProductService productService;



    @PostMapping(value = "/queryProduct")
    @ResponseBody
    public PageResp queryProduct(
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "sort", required = false) String para) {

        List<Integer> a = JSON.parseArray(para,Integer.class);
        return PageResp.success().setData(
                productService.queryProduct(pageNum, pageSize,type,a)
        ).setPage(pageNum, pageSize, productService.queryCount()).setMsg("成功");
    }

    @PostMapping(value = "/addProduct")
    @ResponseBody
    public Resp addProduct(
            @RequestBody Product product
    ) {

        if (product == null ) {
            return Resp.error().setMsg("不能为空");
        }
        try {
            Integer result = productService.addProduct(product);
            return Resp.success().setMsg("插入成功");
        } catch (Exception e) {
            return Resp.error().setMsg(e.getMessage());
        }
    }

    @PostMapping(value = "/editProduct")
    public Result editOrder(
            @RequestBody Product product
    ) {
        if (product == null) {
            return Result.error(CodeEum.PARAM_MISS);
        }
        try {
            productService.editProduct(product);
            return Result.success();
        } catch (Exception e) {
            return Result.error(CodeEum.FAIL);
        }
    }

    @PostMapping(value="/productDetail")
    @ResponseBody
    public Resp queryOrderDetail(
            @RequestParam(value = "productID") String productID,
            @RequestParam(value = "shareID",required = false) String shareID
    ){
//        Long id = Long.valueOf(orderId);
        Map<Object,Object> map =
                productService.productDetail(productID,shareID);

        log.info(String.valueOf(map));
        if (map == null) {
            return Resp.error().setMsg("无数据");
        } else {
            return Resp.success().setData(map).setMsg("成功");
        }
    }

    @PostMapping(value = "/queryProductByKey")
    @ResponseBody
    public Resp queryOrderByKey(
            @RequestParam(value = "keySearch", required = false) String keySearch) {
        List<Map<Object, Object> > map = productService.queryProductByKey(keySearch);
        if (map.size() == 0) {
            return Resp.error().setMsg("无数据");
        } else {
            return Resp.success().setData(
                    map
            ).setMsg("成功");
        }
    }
}
