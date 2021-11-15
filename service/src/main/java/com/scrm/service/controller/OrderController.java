package com.scrm.service.controller;

/**
 * @author Ganyunhui
 * @create 2021-11-03 20:19
 */

import com.scrm.service.service.OrderService;
import com.scrm.service.util.resp.PageResp;
import com.scrm.service.util.resp.Resp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/order")
@RestController
public class OrderController {
    @Resource
    private OrderService orderService;

    @PostMapping(value = "/queryOrderByCustomerID")
    @ResponseBody
    public Resp queryOrderByCustomerID(
            @RequestParam(value = "customerID") String customerID
    ){
        List<Map<Object, Object> > maps = orderService.queryOrderByCustomerID(customerID);
        List<Map<Object, Object> > mapsFinal = new ArrayList<>();
        for(int i=0;i<maps.size();i++) {

            Map<Object, Object> temp;
            temp = maps.get(i);
            StringBuffer s1 = new StringBuffer(String.valueOf(temp.get("orderFinish")));
            String s2 = String.valueOf(temp.get("orderFinish"));
            if(s2=="null"){
                mapsFinal.add(temp);
                continue;
            }
            s1.replace(10, 11, " ");
            temp.remove("orderFinish");
            temp.put("orderFinish",s1);
            mapsFinal.add(temp);
        }

        if (maps.size() == 0) {
            return Resp.error().setMsg("无数据");
        } else {
            return Resp.success().setData(
                    maps
            ).setMsg("成功");
        }
    }

    @PostMapping(value = "/queryOrder")
    @ResponseBody
    public PageResp queryOrder(
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "orderType", required = false) Integer orderType) {
        log.info("orderType:"+String.valueOf(orderType));

        return PageResp.success().setData(
                orderService.queryOrder(pageNum, pageSize, orderType)
        ).setPage(pageNum, pageSize, orderService.queryCount()).setMsg("成功");
    }

    @PostMapping(value="/orderDetail")
    @ResponseBody
    public Resp queryOrderDetail(
            @RequestParam(value = "orderID") String orderId
    ){
//        Long id = Long.valueOf(orderId);
        Map<Object,Object> map =
                orderService.queryOrderDetail(orderId);
        StringBuffer s1=new StringBuffer(String.valueOf(map.get("orderTime")));
        String s3 = String.valueOf(map.get("orderTime"));
        StringBuffer s2=new StringBuffer(String.valueOf(map.get("orderFinish")));
        String s4 = String.valueOf(map.get("orderFinish"));
        if(s3!="null"){
            s1.replace(10,11," ");
            map.remove("orderTime");
            map.put("orderTime",s1);
        }
        if(s4!="null"){
            s2.replace(10,11," ");
            map.remove("orderFinish");
            map.put("orderFinish",s2);
        }

        log.info(String.valueOf(map));
        if (map == null) {
            return Resp.error().setMsg("无数据");
        } else {
            return Resp.success().setData(map).setMsg("成功");
        }
    }

    @PostMapping(value = "/queryOrderByKey")
    @ResponseBody
    public Resp queryOrderByKey(
            @RequestParam(value = "keySearch", required = false) String keySearch,
            @RequestParam(value = "orderType", required = false) Integer orderType) {
        List<Map<Object, Object> > map = orderService.queryOrderByKey(keySearch, orderType);
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
            @RequestParam(value = "userID",required = false) String userID) {
        try {
            Integer result = orderService.deleteOrderWith(orderID);
            result = orderService.deleteOrder(orderID);
            return Resp.success().setMsg("删除成功");
        } catch (Exception e) {
            return Resp.error().setMsg(e.getMessage());
        }
    }
}
