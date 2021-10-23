package com.scrm.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scrm.marketing.entity.ProductCustomerBpLog;
import com.scrm.marketing.mapper.sqlbuilder.ProCusBpLogSqlProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @author fzk
 * @date 2021-10-14 17:58
 */
public interface ProCusBpLogMapper extends BaseMapper<ProductCustomerBpLog> {


    /**
     * 查询log表中不同客户数量
     *
     * @return 返回int
     */
    @Select("SELECT COUNT(DISTINCT customer_id) FROM mk_product_customer_bp_log;")
    int queryCusCount();

    /**
     * 分页查询所有客户购买情况
     * 这里存在一个问题：由于要查出名称和头像，分组就必须把它们也纳入
     * 可是客户表如果修改了头像或者名称，而这个log表并没有级联去修改的话，
     * 就会出现一个customerId却分为了多行的情况
     *
     * @param customerId    客户id optional
     * @param productTypeId 产品分类id optional
     * @param offset        偏移量 optional
     * @param pageSize      页大小 optional
     * @return 返回list
     */
    @SelectProvider(ProCusBpLogSqlProvider.class)
    List<ProductCustomerBpLog> queryCusPurchase(Long customerId, Long productTypeId, Integer offset, Integer pageSize);

    /**
     * 查询产品浏览情况
     * 这里同样有上面描述的分组问题
     *
     * @param productTypeId 产品类型id optional
     * @return 返回list
     */
    @SelectProvider(ProCusBpLogSqlProvider.class)
    List<ProductCustomerBpLog> queryProBrowse(@Nullable Long productTypeId);

    /**
     * 查询客户浏览情况
     * 这里同样有上面描述的分组问题
     *
     * @param customerId 客户id optional
     * @param offset     偏移量 optional
     * @param pageSize   页大小 optional
     * @return 返回list
     */
    @SelectProvider(ProCusBpLogSqlProvider.class)
    List<ProductCustomerBpLog> queryCusBrowse(Long customerId, Integer offset, Integer pageSize);
}
