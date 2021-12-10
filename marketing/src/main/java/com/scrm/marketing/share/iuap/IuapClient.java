package com.scrm.marketing.share.iuap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * 这个类放一些访问Iuap用户信息的方法
 *
 * @author fzk
 * @date 2021-12-10 22:48
 */
@Component
public class IuapClient {
    private final RestTemplate restTemplate;

    public IuapClient() {
        restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN));
        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
    }

    /*获取id失败的话会返回null*/
    public IuapUser getUserById(String id) {
        /*rest微服务调用
        这里不用担心，编译器应该会进行常量折叠
        微服务调用需要在内网中进行,当前先暂时直接写死url*/
        String url = "https://www.fzk-tx.top/cms/user/queryById?id=" + id;
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
