package com.scrm.service.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("se_company_qxb")
public class CompanyQXB {

    private String registerNo;

    private String eid;

    private String companyName;

    private String historyNames;

    private String legalPerson;

    private String belongOrg;

    private String orgNo;

    private String creditNo;

    private String districtCode;

    private String address;

    private String companyKind;

    private String typeNew;

    private String categoryNew;

    private String domain;

    private String scope;

    private String registerCapital;

    private String currencyUnit;

    private String actualCapital;

    private String startDate;

    private String endDate;

    private String checkDate;

    private String revokeDate;

    private String revokeReason;

    private String engageStatus;

    private String statusNew;

    private String termStart;

    private String termEnd;

    private String tags;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String createTime;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String updateTime;
}
