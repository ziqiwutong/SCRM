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
@FeignClient(value = "test", url = "https://mp.weixin.qq.com/")
public interface TestClient {
    @GetMapping(path = "/s?__biz=MzI1NDczNTAwMA==&mid=2247502405&idx=1&sn=5e4a1b8c27cb896cd5c70776421ca5e5&chksm=e9c22db2deb5a4a40105c813f4e556780830adc81e5a408bcfe8df0a6599797ef9d0320abf8b&scene=0&subscene=91&sessionid=1635322172&clicktime=1635322177&enterid=1635322177&ascene=7&devicetype=android-29&version=28000f3d&nettype=WIFI&abtest_cookie=AAACAA%3D%3D&lang=zh_CN&exportkey=A0Xsij9OtMqixJK8RT0ogVs%3D&pass_ticket=OCNRIiwanzCbG4VGtnOXXBWTsbsLpXR1SsiZKFeHVmNsQF7Fi6fMf%2FQbLk9GzIc%2B&wx_header=1")
    public String getArticleDetail(@SpringQueryMap Map<String, Object> param);
}
