package com.scrm.service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;
import java.sql.Timestamp;

public class se_clue {
    long id;
    String clue_name;
    Date clue_date;
    String clue_status;
    String clue_editor;
    String clue_discover;
    String clue_responsible;
    Boolean business_opporitunity_flag;
    Timestamp create_time;
    Timestamp update_time;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClue_name() {
        return clue_name;
    }

    public void setClue_name(String clue_name) {
        this.clue_name = clue_name;
    }

    public Date getClue_date() {
        return clue_date;
    }

    public void setClue_date(Date clue_date) {
        this.clue_date = clue_date;
    }

    public String getClue_status() {
        return clue_status;
    }

    public void setClue_status(String clue_status) {
        this.clue_status = clue_status;
    }

    public String getClue_editor() {
        return clue_editor;
    }

    public void setClue_editor(String clue_editor) {
        this.clue_editor = clue_editor;
    }

    public String getClue_discover() {
        return clue_discover;
    }

    public void setClue_discover(String clue_discover) {
        this.clue_discover = clue_discover;
    }

    public String getClue_responsible() {
        return clue_responsible;
    }

    public void setClue_responsible(String clue_responsible) {
        this.clue_responsible = clue_responsible;
    }

    public Boolean getBusiness_opporitunity_flag() {
        return business_opporitunity_flag;
    }

    public void setBusiness_opporitunity_flag(Boolean business_opporitunity_flag) {
        this.business_opporitunity_flag = business_opporitunity_flag;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Timestamp update_time) {
        this.update_time = update_time;
    }
}
