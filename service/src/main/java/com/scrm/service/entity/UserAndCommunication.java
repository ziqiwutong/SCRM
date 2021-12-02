package com.scrm.service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;
import java.sql.Timestamp;

public class UserAndCommunication {
    private Long id;
    private String customerName;
    private Integer customerType;
    private String labelName;
    private String telephone;
    private String belongCompany;
    private String customerIcon;
    private Date enterPoolDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Integer customerType) {
        this.customerType = customerType;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getBelongCompany() {
        return belongCompany;
    }

    public void setBelongCompany(String belongCompany) {
        this.belongCompany = belongCompany;
    }

    public String getCustomerIcon() { return customerIcon; }

    public void setCustomerIcon(String customerIcon) { this.customerIcon = customerIcon; }

    public Date getEnterPoolDate() {
        return enterPoolDate;
    }

    public void setEnterPoolDate(Date enterPoolDate) {
        this.enterPoolDate = enterPoolDate;
    }
}
