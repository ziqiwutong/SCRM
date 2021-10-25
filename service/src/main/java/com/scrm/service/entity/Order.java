package com.scrm.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author Ganyunhui
 * @create 2021-10-20 21:01
 */
@Data
@TableName("se_order")
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNum;
    private Long customerId;
    private Long productId;
    private String orderStaff;
    private boolean payFlag;
    private boolean successFlag;
    private boolean refundFlag;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updateTime;
}
