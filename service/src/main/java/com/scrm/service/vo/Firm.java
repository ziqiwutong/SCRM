package com.scrm.service.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Firm {

    private String eid;

    private String name;

    private String registerNo;

    private String startDate;

    private String creditNo;

    private String legalPerson;

    private String matchType;

    private String matchItems;

    private int type;
}
