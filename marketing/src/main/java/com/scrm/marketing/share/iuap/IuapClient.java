package com.scrm.marketing.share.iuap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scrm.marketing.util.MyJsonUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

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
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    private static final String IuapUserKey = "iuapUser:";
    private static final Duration cacheTime = Duration.ofMinutes(20L);

    /*获取id失败的话会返回null*/
    public IuapUser getUserById(String id) {
        // 1.先查询缓存
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        String json = opsForValue.get(IuapUserKey + id);
        if (json != null) return MyJsonUtil.toBean(json, IuapUser.class);

        // 2.缓存没有，互斥锁定
        Boolean lockFlag = Boolean.FALSE;
        for (int i = 0; i < 3; i++) {
            lockFlag = opsForValue.setIfAbsent(IuapUserKey + id + "_lock", Thread.currentThread().getName());
            if (Boolean.TRUE.equals(lockFlag)) break;// 拿到锁，退出循环
            try {
                Thread.sleep(ThreadLocalRandom.current().nextLong(0, 500));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 3.拿到锁或循环3次了
        json = opsForValue.get(IuapUserKey + id);

        IuapUser iuapUser = null;
        if (json == null) {// 确实没有，再远程调用
            // 4.rest微服务调用
            String url = "http://manage/user/queryById?id=" + id;
            String result_json = restTemplate.getForObject(url, String.class);

            if (result_json != null) {
                JSONObject jsonObject = JSON.parseObject(result_json);
                if (jsonObject.getInteger("code").equals(200)) {
                    iuapUser = jsonObject.getObject("data", IuapUser.class);
                    // 放缓存
                    json = MyJsonUtil.toJsonStr(iuapUser);
                    opsForValue.set(
                            IuapUserKey + id,
                            json,
                            cacheTime);
                }
            }
        }

        // 5.释放锁
        if (Boolean.TRUE.equals(lockFlag))
            redisTemplate.delete(IuapUserKey + id + "_lock");
        return iuapUser != null ? iuapUser : MyJsonUtil.toBean(json, IuapUser.class);
    }
}
