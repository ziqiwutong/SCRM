package com.scrm.service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class Communication {
    private Long id;
    private Long customerId;
    private Integer visitTimes;
    private Integer callTimes;
    private Integer msgTimes;
    private Integer wxTimes;
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

    public Integer getVisitTimes() { return visitTimes; }

    public void setVisitTimes(Integer visitTimes) {
        this.visitTimes = visitTimes;
    }

    public Integer getCallTimes() {
        return callTimes;
    }

    public void setCallTimes(Integer callTimes) {
        this.callTimes = callTimes;
    }

    public Integer getMsgTimes() {
        return msgTimes;
    }

    public void setMsgTimes(Integer msgTimes) {
        this.msgTimes = msgTimes;
    }

    public Integer getWxTimes() {
        return wxTimes;
    }

    public void setWxTimes(Integer wxTimes) {
        this.wxTimes = wxTimes;
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
