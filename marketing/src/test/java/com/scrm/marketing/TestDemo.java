package com.scrm.marketing;

import com.scrm.marketing.util.resp.Result;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * @author fzk
 * @date 2021-10-19 19:37
 */
public class TestDemo {

    @Test
    void test2() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:30001/file/pic/base64StrToPic";

        // 请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 请求体
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

        File file = new File("D:/mynginx/1.jpg");
        try (FileInputStream in = new FileInputStream(file)) {
            byte[] buffer = in.readAllBytes();

            buffer = Base64.getEncoder().encode(buffer);

            String picBase64Str = "data:image/png;base64," + new String(buffer);
            buffer=null;

            map.add("picBase64Str", picBase64Str);
            map.add("picType", "userIcon");
            map.add("picFormat", "jpg");

            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);

            try {
                Result result = restTemplate.postForObject(url, httpEntity, Result.class);
                System.out.println(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
