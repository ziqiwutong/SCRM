package com.scrm.service.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FirmRelation {

    private int direction;

    private String target;

    private String label;
}
