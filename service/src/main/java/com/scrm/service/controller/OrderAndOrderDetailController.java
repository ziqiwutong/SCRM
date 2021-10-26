package com.scrm.service.controller;

import com.scrm.service.entity.BusinessOpportunity;
import com.scrm.service.entity.OrderAndOrderDetail;
import com.scrm.service.service.OrderAndOrderDetailService;
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
 * @create 2021-10-20 21:57
 */
@Slf4j
@RequestMapping("/order")
@RestController
public class OrderAndOrderDetailController {
    @Resource
    OrderAndOrderDetailService orderAndOrderDetailService;

    @PostMapping(value="/queryOrderById")
    @ResponseBody
    public Resp queryOrderById(
            @RequestParam(value = "orderId") String orderId
    ){
//        Long id = Long.valueOf(orderId);
        Map<Object,Object> map =
                orderAndOrderDetailService.queryOrderAndOrderDetail(orderId);
        log.info(String.valueOf(map));
        if (map == null) {
            return Resp.error().setMsg("无数据");
        } else {
            return Resp.success().setData(map).setMsg("成功");
        }
    }

    @PostMapping(value = "/queryOrder")
    @ResponseBody
    public PageResp queryOrder(
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "orderType", required = false) Integer orderType) {

        return PageResp.success().setData(
                orderAndOrderDetailService.queryOrder(pageNum, pageSize, orderType)
        ).setPage(pageNum, pageSize, orderAndOrderDetailService.queryCount()).setMsg("成功");
    }

    @PostMapping(value = "/queryOrderByKey")
    @ResponseBody
    public Resp queryOrderByKey(
            @RequestParam(value = "keySearch", required = false) String keySearch,
            @RequestParam(value = "orderType", required = false) Integer orderType) {
        List<Map<Object, Object> > map = orderAndOrderDetailService.queryOrderByKey(keySearch, orderType);
//        List<BusinessOpportunity> se_business_opportunity = se_business_opportunityService.queryBizOppByKey(keySearch);
        if (map.size() == 0) {
            return Resp.error().setMsg("无数据");
        } else {
            return Resp.success().setData(
                    map
            ).setMsg("成功");
        }
    }

    @PostMapping(value = "/deleteOrder")
    @ResponseBody
    public Resp deleteOrder(
            @RequestParam(value = "orderID") String orderID,
            @RequestParam(value = "userID") String userID) {
        try {
            Integer result = orderAndOrderDetailService.deleteOrderWith(orderID);
            result = orderAndOrderDetailService.deleteOrder(orderID);
            return Resp.success().setMsg("删除成功");
        } catch (Exception e) {
            return Resp.error().setMsg(e.getMessage());
        }
    }

    @PostMapping(value = "/addOrder")
    @ResponseBody
    public Resp addOrder(
            @RequestBody OrderAndOrderDetail orderAndOrderDetail
    ) {
//        log.info(orderAndOrderDetail.toString());
        if (orderAndOrderDetail == null ) {
            return Resp.error().setMsg("不能为空");
        }
        try {
            Integer result = orderAndOrderDetailService.addOrder(orderAndOrderDetail);
            result = orderAndOrderDetailService.addOrderWith(orderAndOrderDetail);

            return Resp.success().setMsg("插入成功");
        } catch (Exception e) {
            return Resp.error().setMsg(e.getMessage());
        }
    }

    @PostMapping(value = "/editOrder")
    public Result editOrder(
            @RequestBody OrderAndOrderDetail orderAndOrderDetail
    ) {
        if (orderAndOrderDetail == null) {
            return Result.error(CodeEum.PARAM_MISS);
        }
        try {
            orderAndOrderDetailService.editOrderWith(orderAndOrderDetail);
            orderAndOrderDetailService.editOrder(orderAndOrderDetail);
            return Result.success();
        } catch (Exception e) {
            return Result.error(CodeEum.FAIL);
        }
    }

}
