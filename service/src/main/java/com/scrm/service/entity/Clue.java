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
    private  String clueEditor;
    private  String clueDiscover;
    private  String clueResponsible;
    private  Boolean businessOpporitunityFlag;
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

    public String getClueEditor() {
        return clueEditor;
    }

    public void setClueEditor(String clueEditor) {
        this.clueEditor = clueEditor;
    }

    public String getClueDiscover() {
        return clueDiscover;
    }

    public void setClueDiscover(String clueDiscover) {
        this.clueDiscover = clueDiscover;
    }

    public String getClueResponsible() {
        return clueResponsible;
    }

    public void setClueResponsible(String clueResponsible) {
        this.clueResponsible = clueResponsible;
    }

    public Boolean getBusinessOpporitunityFlag() {
        return businessOpporitunityFlag;
    }

    public void setBusinessOpporitunityFlag(Boolean businessOpporitunityFlag) {
        this.businessOpporitunityFlag = businessOpporitunityFlag;
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
