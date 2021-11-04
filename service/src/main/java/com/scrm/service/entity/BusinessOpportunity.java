package com.scrm.service.entity;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class BusinessOpportunity {
    private Long id;
    private Long customerId;
    private String boName;
    private Date boDate;
    private String boStatus;
    private Integer boFollowStatus;
    private String boEditor;
    private String boTemplate;
    private String boResponsible;
    private BigDecimal boAmount;
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

    public String getBoName() {
        return boName;
    }

    public void setBoName(String boName) {
        this.boName = boName;
    }

    public Date getBoDate() {
        return boDate;
    }

    public void setBoDate(Date boDate) {
        this.boDate = boDate;
    }

    public String getBoStatus() {
        return boStatus;
    }

    public void setBoStatus(String boStatus) {
        this.boStatus = boStatus;
    }

    public Integer getBoFollowStatus() {
        return boFollowStatus;
    }

    public void setBoFollowStatus(Integer boFollowStatus) {
        this.boFollowStatus = boFollowStatus;
    }

    public String getBoEditor() {
        return boEditor;
    }

    public void setBoEditor(String boEditor) {
        this.boEditor = boEditor;
    }

    public String getBoTemplate() {
        return boTemplate;
    }

    public void setBoTemplate(String boTemplate) {
        this.boTemplate = boTemplate;
    }

    public String getBoResponsible() {
        return boResponsible;
    }

    public void setBoResponsible(String boResponsible) {
        this.boResponsible = boResponsible;
    }

    public BigDecimal getBoAmount() {
        return boAmount;
    }

    public void setBoAmount(BigDecimal boAmount) {
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
