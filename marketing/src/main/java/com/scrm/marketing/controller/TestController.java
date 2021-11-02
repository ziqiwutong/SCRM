package com.scrm.marketing.controller;

import com.scrm.marketing.feign.TestClient;
import com.scrm.marketing.util.MyDateTimeUtil;
import com.scrm.marketing.util.resp.CodeEum;
import com.scrm.marketing.util.resp.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fzk
 * @date 2021-10-27 18:02
 */
@RestController
@RequestMapping(path = "/mk/test")
public class TestController {
    @Resource
    private TestClient testClient;

    @RequestMapping(path = "/article")
    public Result test(HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String param = request.getParameter("articleUrl");
            System.out.println(param);
//            BufferedReader reader = request.getReader();


            String result = restTemplate.getForObject(param, String.class);


            int js_content = result.indexOf("<div class=\"rich_media_content \" id=\"js_content\"");
            System.out.println(js_content);
            result = result.substring(js_content);

//        result = result.replaceAll("\\n|\\t", " ");
            result = result.replaceAll(">(\\s+)<", "><");

            int lastIndex = result.indexOf("</section></div>");
            System.out.println(lastIndex);
            if (lastIndex != -1)
                result = result.substring(0, lastIndex + 16);


            Map<String, Object> data = new HashMap<>();
            data.put("html", result);
            data.put("date", MyDateTimeUtil.getNowDateOrTime("yyyy-MM-dd"));
            data.put("author", "fzk");
            data.put("title", "fzk");
            return Result.success(data);
        } catch (Exception e) {
            return Result.error(CodeEum.PARAM_ERROR);
        }
    }

    @GetMapping(path = "/article/huke")
    public Result huke(HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String url="https://huke.163.com/wxArtilceParse2";
            String param = request.getParameter("articleUrl");
            url+="?articleUrl="+param;
            Map map = restTemplate.getForObject(url, Map.class);
            return Result.success(map.get("data"));
        } catch (Exception e) {
            return Result.error(CodeEum.PARAM_ERROR);
        }
    }
}
