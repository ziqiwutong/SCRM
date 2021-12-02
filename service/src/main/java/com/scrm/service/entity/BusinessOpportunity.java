package com.scrm.service.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class BusinessOpportunity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long customerId;
    private String customerName;
    private String boName;
    private String boStatus;
    private String boFullStage;
    private String boFollowStage;
    private Long boEditorId;
    private String boEditor;
    private Long boResponsibleId;
    private String boResponsible;
    private String boAmount;
    private Date boExpectDate;
    private String boNotes;
    private Timestamp createTime;
    private Timestamp updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBoName() {
        return boName;
    }

    public void setBoName(String boName) {
        this.boName = boName;
    }

    public String getBoStatus() {
        return boStatus;
    }

    public void setBoStatus(String boStatus) {
        this.boStatus = boStatus;
    }

    public String getBoFullStage() {
        return boFullStage;
    }

    public void setBoFullStage(String boFullStage) {
        this.boFullStage = boFullStage;
    }

    public String getBoFollowStage() {
        return boFollowStage;
    }

    public void setBoFollowStage(String boFollowStage) {
        this.boFollowStage = boFollowStage;
    }

    public Long getBoEditorId() {
        return boEditorId;
    }

    public void setBoEditorId(Long boEditorId) {
        this.boEditorId = boEditorId;
    }

    public String getBoEditor() {
        return boEditor;
    }

    public void setBoEditor(String boEditor) {
        this.boEditor = boEditor;
    }

    public Long getBoResponsibleId() {
        return boResponsibleId;
    }

    public void setBoResponsibleId(Long boResponsibleId) {
        this.boResponsibleId = boResponsibleId;
    }

    public String getBoResponsible() {
        return boResponsible;
    }

    public void setBoResponsible(String boResponsible) {
        this.boResponsible = boResponsible;
    }

    public String getBoAmount() {
        return boAmount;
    }

    public void setBoAmount(String boAmount) {
        this.boAmount = boAmount;
    }

    public Date getBoExpectDate() {
        return boExpectDate;
    }

    public void setBoExpectDate(Date boExpectDate) {
        this.boExpectDate = boExpectDate;
    }

    public String getBoNotes() {
        return boNotes;
    }

    public void setBoNotes(String boNotes) {
        this.boNotes = boNotes;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
