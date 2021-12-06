package com.scrm.service.dao;

import com.scrm.service.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Map;

/**
 * @author Ganyunhui
 * @create 2021-11-03 20:20
 */
@Mapper
public interface OrderDao {
    List<Map<Object, Object>> queryOrder(Integer pageNum, Integer pageSize, Integer orderType);

    Integer queryCount();

    Map<Object, Object> queryOrderDetail(String id);

    List<Map<Object, Object>> queryOrderByKey(String keySearch, Integer orderType);

    Integer deleteOrder(String orderID);

    Integer deleteOrderWith(String orderID);

    List<Map<Object, Object>> queryOrderByCustomerID(String customerID);

    @Insert("insert into se_order (" +
            "order_num, customer_id, customer_name, order_staff, origin_price, change_price, " +
            "last_price, received_amount, pay_time, sale_channel, order_source, order_status" +
            ") values (" +
            "#{orderNum}, #{customerId}, #{customerName}, #{orderStaff}, #{originPrice}, #{changePrice} " +
            ", #{lastPrice}, #{receivedAmount}, #{payTime}, #{saleChannel}, #{orderSource}, #{orderStatus}" +
            ")")
    void addOrder(Order order);

    @Update("update se_order set " +
            "order_num = #{orderNum}, customer_id = #{customerId}, customer_name = #{customerName}, " +
            "order_staff = #{orderStaff}, origin_price = #{originPrice}, change_price = #{changePrice}, " +
            "last_price = #{lastPrice}, received_amount = #{receivedAmount}, pay_time = #{payTime}, " +
            "sale_channel = #{saleChannel}, order_source = #{orderSource}, order_status = #{orderStatus} " +
            "where id = #{id}")
    void editOrder(Order order);

    @Select("select * from se_order where order_num = #{orderNum}")
    List<Order> queryByOrderNum(@NonNull String orderNum);

}
