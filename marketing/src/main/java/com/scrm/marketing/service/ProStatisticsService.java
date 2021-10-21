package com.scrm.marketing.service;

import com.scrm.marketing.util.resp.Result;
import org.springframework.lang.Nullable;

/**
 * @author fzk
 * @date 2021-10-20 21:31
 */
public interface ProStatisticsService {
    Result queryProPurchase(@Nullable Long typeId, @Nullable Integer pageNum, @Nullable Integer pageSize);

    Result queryProBrowse(@Nullable Long productTypeId);
}
