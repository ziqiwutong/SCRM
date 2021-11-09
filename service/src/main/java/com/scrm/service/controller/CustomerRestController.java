package com.scrm.service.controller;

import com.scrm.service.service.CustomerRestService;
import com.scrm.service.util.resp.Resp;
import com.scrm.service.vo.FirmRelation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;

@RequestMapping("/customerRest")
@RestController
public class CustomerRestController {

    @Resource
    private CustomerRestService customerRestService;

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
