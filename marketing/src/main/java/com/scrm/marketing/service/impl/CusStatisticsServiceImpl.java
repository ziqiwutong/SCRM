package com.scrm.marketing.service.impl;

import cn.hutool.core.date.DateUtil;
import com.scrm.marketing.entity.ArticleCustomerRead;
import com.scrm.marketing.entity.ProductCustomerBpLog;
import com.scrm.marketing.mapper.ArtCusReadMapper;
import com.scrm.marketing.mapper.ProCusBpLogMapper;
import com.scrm.marketing.service.CusStatisticsService;
import com.scrm.marketing.util.resp.CodeEum;
import com.scrm.marketing.util.resp.PageResult;
import com.scrm.marketing.util.resp.Result;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * @author fzk
 * @date 2021-10-22 17:01
 */
@Service
public class CusStatisticsServiceImpl implements CusStatisticsService {
    @Resource
    private ProCusBpLogMapper bpLogMapper;
    @Resource
    private ArtCusReadMapper artCusReadMapper;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Result queryCusRead(Long customerId, Boolean sevenFlag, Integer pageNum, Integer pageSize) {
        List<ArticleCustomerRead> artCusReads;
        // 情况1：无customerId，分页查询客户阅读总时长
        if (customerId == null) {
            // 1.计算偏移量
            int offset = (pageNum - 1) * pageSize;

            // 2.分页查询
            artCusReads = artCusReadMapper.queryCusRead(null, null, offset, pageSize);

            // 3.查询mk_article_customer_read表里的客户总数
            int total = artCusReadMapper.queryCusCount();
            return PageResult.success(artCusReads, total, pageNum);
        }
        // 情况2：有customerId，sevenFlag为true，查1周内客户每天阅读时长
        // 情况3：有customerId，sevenFlag为false，查1个月内客户每天阅读时长
        LocalDate startDate = LocalDate.now();
        startDate = sevenFlag ? startDate.minusDays(7L) : startDate.minusDays(30L);

        artCusReads = artCusReadMapper.queryCusRead(customerId, startDate, null, null);
        return Result.success(artCusReads);
    }


    @Override
    public PageResult queryCusPurchase(Integer pageNum, Integer pageSize) {
        // 计算偏移量
        int offset = (pageNum - 1) * pageSize;
        // 分页查询
        List<ProductCustomerBpLog> bpLogs =
                bpLogMapper.queryCusPurchase(null, null, offset, pageSize);
        // 查询log表的客户数
        int total = bpLogMapper.queryCusCount();
        return PageResult.success(bpLogs, total, pageNum);
    }

    @Override
    public Result queryCusPurchase(Long customerId) {
        List<ProductCustomerBpLog> bpLogs = bpLogMapper.queryCusPurchase(customerId, null, null, null);
        return Result.success(bpLogs);
    }

    @Override
    public Result queryCusPurchase(Long customerId, Long productTypeId) {
        List<ProductCustomerBpLog> bpLogs = bpLogMapper.queryCusPurchase(customerId, productTypeId, null, null);
        return Result.success(bpLogs);
    }

    @Override
    public Result queryCusBrowse(Long customerId, Integer pageNum, Integer pageSize) {
        List<ProductCustomerBpLog> bpLogs;
        // 情况1：分页查询客户浏览总时长
        if (customerId == null) {
            if (pageNum == null || pageSize == null)
                return Result.error(CodeEum.PARAM_MISS);
            // 计算偏移量
            int offset = (pageNum - 1) * pageSize;
            // 查询bpLogs
            bpLogs = bpLogMapper.queryCusBrowse(null, offset, pageSize);
            // 查询bpLogs表的总的客户数
            int total = bpLogMapper.queryCusCount();
            return PageResult.success(bpLogs, total, pageNum);
        }
        // 情况2：查询某个客户浏览每个产品分类总时长
        else {
            bpLogs = bpLogMapper.queryCusBrowse(customerId, null, null);
            return Result.success(bpLogs);
        }
    }
}
