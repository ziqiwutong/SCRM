package com.scrm.service.entity;

import com.scrm.service.vo.FirmRelation;

import java.util.ArrayList;

public class RelationScore {
    private String score;
    private ArrayList<FirmRelation> firmRelation;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public ArrayList<FirmRelation> getFirmRelation() {
        return firmRelation;
    }

    public void setFirmRelation(ArrayList<FirmRelation> firmRelation) {
        this.firmRelation = firmRelation;
    }
}
