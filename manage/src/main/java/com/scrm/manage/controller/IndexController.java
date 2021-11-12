package com.scrm.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author fzk
 * @date 2021-11-10 18:29
 */
@Controller
public class IndexController {
    @RequestMapping("/file/index")
    public String index() {
        return "index";
    }
}
