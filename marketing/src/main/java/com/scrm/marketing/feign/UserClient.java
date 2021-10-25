package com.scrm.marketing.feign;

import com.scrm.marketing.feign.fallback.UserClientFallback;
import com.scrm.marketing.util.resp.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author fzk
 * @date 2021-10-15 18:38
 */
@FeignClient(value = "service",fallback = UserClientFallback.class)
// 作为feign的接口，找service服务
// 还可以使用url属性（绝对值或仅主机名）指定 URL
public interface UserClient {
    @GetMapping("/user/detail")
    public Result getUser(@RequestParam("id") Long id);
}
