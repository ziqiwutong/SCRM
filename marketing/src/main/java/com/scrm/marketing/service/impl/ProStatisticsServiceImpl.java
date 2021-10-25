package com.scrm.marketing.service.impl;

import com.scrm.marketing.entity.Product;
import com.scrm.marketing.entity.ProductCustomerBpLog;
import com.scrm.marketing.mapper.ProCusBpLogMapper;
import com.scrm.marketing.mapper.ProductMapper;
import com.scrm.marketing.service.ProStatisticsService;
import com.scrm.marketing.util.resp.CodeEum;
import com.scrm.marketing.util.resp.PageResult;
import com.scrm.marketing.util.resp.Result;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fzk
 * @date 2021-10-20 21:31
 */
@Service
public class ProStatisticsServiceImpl implements ProStatisticsService {
    @Resource
    private ProductMapper productMapper;
    @Resource
    private ProCusBpLogMapper proCusBpLogMapper;

    @Override
    public Result queryProPurchase(@Nullable Long typeId, @Nullable Integer pageNum, @Nullable Integer pageSize) {
        List<Product> products = null;
        // 1、如果typeId不为空
        if (typeId != null) {
            products = productMapper.queryByTypeId(typeId);
            return Result.success(products);
        }
        // 2.计算偏移量
        if (pageNum == null || pageSize == null || pageNum < 1 || pageSize < 1)
            return Result.error(CodeEum.PARAM_ERROR);
        int offset = (pageNum - 1) * pageSize;
        // 3.分页查询
        products = productMapper.queryProPurchase(offset, pageSize);

        // 4.查询总数
        Integer total = productMapper.selectCount(null);
        return PageResult.success(products, total, pageNum);
    }

    @Override
    public Result queryProBrowse(@Nullable Long productTypeId) {
        // 将对productTypeId 的判断交由SQL构造器去判断，以此构造不同SQL语句
        List<ProductCustomerBpLog> bpLogs = proCusBpLogMapper.queryProBrowse(productTypeId);

        return Result.success(bpLogs);
    }
}
