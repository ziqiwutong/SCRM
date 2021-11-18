package com.scrm.service.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PhoneAttribution {

    private int segment;

    private String type;

    private String operator;

    private String province;

    private String city;

    private String areaCode;

    private String cityCode;

    private String zipCode;
}
