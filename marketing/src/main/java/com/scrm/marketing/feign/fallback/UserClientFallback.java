package com.scrm.marketing.feign.fallback;

import com.scrm.marketing.feign.UserClient;
import com.scrm.marketing.util.resp.CodeEum;
import com.scrm.marketing.util.resp.Result;

/**
 * @author fzk
 * @date 2021-10-15 19:36
 */
public class UserClientFallback implements UserClient {
    @Override
    public Result getUser(Long id) {
        // 返回请求超时信息
        return Result.error(CodeEum.TIMEOUT);
    }
}
