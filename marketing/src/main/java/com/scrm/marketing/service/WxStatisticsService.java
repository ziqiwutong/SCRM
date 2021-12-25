package com.scrm.marketing.service;

import com.scrm.marketing.entity.WxReadRecord;

import java.util.List;

/**
 * @author fzk
 * @date 2021-12-25 12:19
 */
public interface WxStatisticsService {
    List<WxReadRecord> queryWxRead(Long wid,int pageNum, int pageSize, boolean sevenFlag);
}
