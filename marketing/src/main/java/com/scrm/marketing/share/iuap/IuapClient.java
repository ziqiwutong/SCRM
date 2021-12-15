package com.scrm.marketing.share.iuap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * 这个类放一些访问Iuap用户信息的方法
 *
 * @author fzk
 * @date 2021-12-10 22:48
 */
@Component
public class IuapClient {
    @Resource
    private RestTemplate restTemplate;

    /*获取id失败的话会返回null*/
    public IuapUser getUserById(String id) {
        /*rest微服务调用
        微服务调用需要在内网中进行*/
        String url = "http://manage/user/queryById?id=" + id;
        IuapUser iuapUser = null;

        String result_json = restTemplate.getForObject(url, String.class);

        if (result_json != null) {
            JSONObject jsonObject = JSON.parseObject(result_json);
            if (jsonObject.getInteger("code").equals(200)) {
                iuapUser = jsonObject.getObject("data", IuapUser.class);
            }
        }

        return iuapUser;
    }
}
