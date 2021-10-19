package com.scrm.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("se_customer")
public class Customer {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Integer customerType;

    private Integer potential;

    private String customerName;

    private String historyName;

    private String customerIcon;

    private String telephone;

    private String city;

    private String customerStatus;

    private Long followStaffId;

    private String followStaffName;

    private String enterPoolData;

    private String origin;

    private BigDecimal orderAmount;

    private Integer orderNumber;

    private String customizeField;

    /* 个人客户字段 */
    private String numberAttribution;

    private String wx;

    private String wxName;

    private String sex;

    private String ageRange;

    private String birthday;

    private String position;

    private String hobby;

    private String customerLevel;

    /* 企业客户字段 */
    private String legalPerson;

    private String operatingStatus;

    private String establishDate;

    private String approvalDate;

    private String registeredCapital;

    private String paidCapital;

    private String companyType;

    private String companySize;

    private String industry;

    private String industryCode;

    private String address;

    private String businessRange;

    private String socialCreditCode;

    private String organizationCode;

    private String registrationAuthority;

    /* 潜在客户字段 */
    private String potentialType;

    private String creditStatus;

    private String createTime;

    private String updateTime;
}
