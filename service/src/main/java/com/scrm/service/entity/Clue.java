package com.scrm.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;
import java.sql.Timestamp;

public class Clue {
    @TableId(type = IdType.AUTO)
    private  Long id;
    private  String clueName;
    private  Date clueDate;
    private  String clueStatus;
    private  Long clueEditorId;
    private  String clueEditor;
    private  Long clueDiscoverId;
    private  String clueDiscover;
    private  Long clueResponsibleId;
    private  String clueResponsible;
    private  Boolean bizOppFlag;
    private  Timestamp createTime;
    private  Timestamp updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClueName() {
        return clueName;
    }

    public void setClueName(String clueName) {
        this.clueName = clueName;
    }

    public Date getClueDate() {
        return clueDate;
    }

    public void setClueDate(Date clueDate) {
        this.clueDate = clueDate;
    }

    public String getClueStatus() {
        return clueStatus;
    }

    public void setClueStatus(String clueStatus) {
        this.clueStatus = clueStatus;
    }

    public Long getClueEditorId() { return clueEditorId; }

    public void setClueEditorId(Long clueEditorId) { this.clueEditorId = clueEditorId; }

    public String getClueEditor() {
        return clueEditor;
    }

    public void setClueEditor(String clueEditor) {
        this.clueEditor = clueEditor;
    }

    public Long getClueDiscoverId() { return clueDiscoverId; }

    public void setClueDiscoverId(Long clueDiscoverId) { this.clueDiscoverId = clueDiscoverId; }

    public String getClueDiscover() {
        return clueDiscover;
    }

    public void setClueDiscover(String clueDiscover) {
        this.clueDiscover = clueDiscover;
    }

    public Long getClueResponsibleId() { return clueResponsibleId; }

    public void setClueResponsibleId(Long clueResponsibleId) { this.clueResponsibleId = clueResponsibleId; }

    public String getClueResponsible() {
        return clueResponsible;
    }

    public void setClueResponsible(String clueResponsible) {
        this.clueResponsible = clueResponsible;
    }

    public Boolean getBizOppFlag() {
        return bizOppFlag;
    }

    public void setBizOppFlag(Boolean bizOppFlag) {
        this.bizOppFlag = bizOppFlag;
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
