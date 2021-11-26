package com.scrm.service.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BusinessCard {

    private Object[] zipCode;

    private Object[] telephone;

    private Object[] title;

    private Object[] email;

    private Object[] fax;

    private Object[] url;

    private Object[] address;

    private Object[] company;

    private Object[] mobile;

    private Object[] name;
}
