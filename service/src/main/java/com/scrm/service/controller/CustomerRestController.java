package com.scrm.service.controller;

import com.scrm.service.service.CustomerRestService;
import com.scrm.service.util.resp.Resp;
import com.scrm.service.vo.FirmRelation;
import com.scrm.service.vo.PhoneAttribution;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;

@RequestMapping("/customerRest")
@RestController
public class CustomerRestController {

    @Resource
    private CustomerRestService customerRestService;

    @GetMapping("/attribution")
    @ResponseBody
    public Resp queryPhoneAttribution(
            @RequestParam(value = "phone") String phone
    ) {
        PhoneAttribution attribution = customerRestService.queryPhoneAttribution(phone);
        if (attribution != null) {
            return Resp.success().setData(attribution);
        } else {
            return Resp.error().setMsg("获取失败");
        }
    }

    @GetMapping("/businessCard")
    @ResponseBody
    public Resp scanBusinessCard(
            @RequestParam(value = "image") String url
    ) {
        String info = customerRestService.scanBusinessCard(url);
        if (info != null) {
            return Resp.success().setData(info);
        } else {
            return Resp.error().setMsg("获取失败");
        }
    }

    @GetMapping("/relation")
    @ResponseBody
    public Resp queryRelationById(
            @RequestParam(value = "firmA") String firmA,
            @RequestParam(value = "firmB") String firmB
    ) {
        ArrayList<ArrayList<FirmRelation>> relation = customerRestService.queryRelationBetweenFirm(
                firmA.replace(',', '，'),
                firmB.replace(',', '，')
        );
        if (relation != null) {
            return Resp.success().setData(relation);
        } else {
            return Resp.error().setMsg("获取失败");
        }
    }
}
