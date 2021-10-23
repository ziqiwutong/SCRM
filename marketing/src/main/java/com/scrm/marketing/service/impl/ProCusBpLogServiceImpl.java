package com.scrm.marketing.service.impl;

import com.scrm.marketing.entity.ProductCustomerBpLog;
import com.scrm.marketing.mapper.ProCusBpLogMapper;
import com.scrm.marketing.service.ProCusBpLogService;
import com.scrm.marketing.util.resp.CodeEum;
import com.scrm.marketing.util.resp.PageResult;
import com.scrm.marketing.util.resp.Result;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fzk
 * @date 2021-10-20 22:38
 */
@Service
public class ProCusBpLogServiceImpl implements ProCusBpLogService {
    @Resource
    private ProCusBpLogMapper bpLogMapper;

    @Override
    public PageResult queryCusPurchase(@NonNull Integer pageNum, @NonNull Integer pageSize) {
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
    public Result queryCusPurchase(@NonNull Long customerId) {
        List<ProductCustomerBpLog> bpLogs = bpLogMapper.queryCusPurchase(customerId, null, null, null);
        return Result.success(bpLogs);
    }

    @Override
    public Result queryCusPurchase(@NonNull Long customerId, @NonNull Long productTypeId) {
        List<ProductCustomerBpLog> bpLogs = bpLogMapper.queryCusPurchase(customerId, productTypeId, null, null);
        return Result.success(bpLogs);
    }

    @Override
    public Result queryCusBrowse(@Nullable Long customerId, @Nullable Integer pageNum, @Nullable Integer pageSize) {
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
