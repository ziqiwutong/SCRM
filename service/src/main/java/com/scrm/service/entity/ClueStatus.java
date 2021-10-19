package com.scrm.service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class ClueStatus {
    private Long id;
    private Long clueId;
    private String clueEditor;
    private String clueStatus;
    private String clueNotes;
    private Timestamp createTime;
    private Timestamp updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) { this.id = id; }

    public Long getClueId() {
        return clueId;
    }

    public void setClueId(Long clueId) {
        this.clueId = clueId;
    }

    public String getClueEditor() {
        return clueEditor;
    }

    public void setClueEditor(String clueEditor) {
        this.clueEditor = clueEditor;
    }

    public String getClueStatus() {
        return clueStatus;
    }

    public void setClueStatus(String clueStatus) {
        this.clueStatus = clueStatus;
    }

    public String getClueNotes() {
        return clueNotes;
    }

    public void setClueNotes(String clueNotes) {
        this.clueNotes = clueNotes;
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
