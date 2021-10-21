package com.scrm.marketing.feign;

import com.scrm.marketing.util.resp.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * @author fzk
 * @date 2021-10-20 20:13
 */
@FeignClient(value = "test", url = "https://www.fzk-tx.top")
public interface TestClient {
    @GetMapping(path = "/mk/article/detail", headers = {"Authorization=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySUQiOiJzY3JtMTIzIiwiaWF0IjoxNjM0NjQ2OTEwLCJleHAiOjE2MzUyNTE3MTB9.C3Tp_mN2u_K5erHX3LZj_hp5rR2hr4iUHR_ieZqCY7k"})
    public Result getArticleDetail(@SpringQueryMap Map<String, Object> param);
}
