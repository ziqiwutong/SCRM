package com.scrm.service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class se_clue_status {
    long id;
    long clue_id;
    String clue_editor;
    String clue_status;
    String clue_notes;
    Timestamp create_time;
    Timestamp update_time;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getClue_id() {
        return clue_id;
    }

    public void setClue_id(long clue_id) {
        this.clue_id = clue_id;
    }

    public String getClue_editor() {
        return clue_editor;
    }

    public void setClue_editor(String clue_editor) {
        this.clue_editor = clue_editor;
    }

    public String getClue_follow_status() {
        return clue_status;
    }

    public void setClue_follow_status(String clue_follow_status) {
        this.clue_status = clue_follow_status;
    }

    public String getClue_notes() {
        return clue_notes;
    }

    public void setClue_notes(String clue_notes) {
        this.clue_notes = clue_notes;
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
