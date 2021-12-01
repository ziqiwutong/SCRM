package com.scrm.service.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("se_phone_attribution")
public class PhoneAttribution {

    private String phone;

    private int segment;

    private String type;

    private String operator;

    private String province;

    private String city;

    private String areaCode;

    private String cityCode;

    private String zipCode;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String createTime;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String updateTime;
}
