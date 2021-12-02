package com.scrm.service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;
import java.sql.Timestamp;

public class CommunicationLog {
    private Long id;
    private Long customerId;
    private Integer communicationWay;
    private String communicationContent;
    private Timestamp communicationTime;
    private Timestamp createTime;
    private  Timestamp updateTime;

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

    public Integer getCommunicationWay() {
        return communicationWay;
    }

    public void setCommunicationWay(Integer communicationWay) {
        this.communicationWay = communicationWay;
    }

    public String getCommunicationContent() {
        return communicationContent;
    }

    public void setCommunicationContent(String communicationContent) {
        this.communicationContent = communicationContent;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp getCommunicationTime() { return communicationTime; }

    public void setCommunicationTime(Timestamp communicationTime) { this.communicationTime = communicationTime; }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) { this.createTime = createTime; }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
