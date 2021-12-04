package com.scrm.marketing;

import com.scrm.marketing.util.resp.Result;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author fzk
 * @date 2021-10-19 19:37
 */
public class TestDemo {

    @Test
    void test1() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.fzk-tx.top/mk/article/addReadRecord";

        // 请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 请求体
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("articleId", 1);
        map.add("shareId", 1);
        map.add("openid", 1);
        map.add("readTime", 1);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);

        try {
            restTemplate.postForObject(url, httpEntity, Void.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
