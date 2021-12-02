package com.scrm.service.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidubce.http.ApiExplorerClient;
import com.baidubce.http.AppSigner;
import com.baidubce.http.HttpMethodName;
import com.baidubce.model.ApiExplorerRequest;
import com.baidubce.model.ApiExplorerResponse;
import com.scrm.service.service.WeimobService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class WeimobServiceImpl implements WeimobService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Value("${docking.weimob.client.id}")
    private String clientId;

    @Value("${docking.weimob.client.secret}")
    private String clientSecret;

    @Override
    public String queryProduct() {
        String token = getToken();
        System.out.println(token);
        return token;
    }

    /**
     * 获取access-token，用redis缓存
     */
    private String getToken() {
        String token = stringRedisTemplate.opsForValue().get("weimob");
        if (token == null) {
            ApiExplorerClient client = new ApiExplorerClient(new AppSigner());

            String path = "https://dopen.weimob.com/fuwu/b/oauth2/token";
            ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.POST, path);
            request.addHeaderParameter("Content-Type", "application/json;charset=UTF-8");
            request.addQueryParameter("grant_type", "client_credentials");
            request.addQueryParameter("client_id", clientId);
            request.addQueryParameter("client_secret", clientSecret);
            try {
                ApiExplorerResponse response = client.sendRequest(request);
                JSONObject body = JSONObject.parseObject(response.getResult());
                token = body.getString("access_token");
                stringRedisTemplate.opsForValue().set(
                        "weimob",
                        token,
                        body.getLong("expires_in") - 10,
                        TimeUnit.SECONDS
                );
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return token;
    }
}
