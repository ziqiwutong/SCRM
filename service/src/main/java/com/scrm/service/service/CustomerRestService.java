package com.scrm.service.service;

import com.scrm.service.entity.CompanyQXB;
import com.scrm.service.vo.BusinessCard;
import com.scrm.service.vo.FirmRelation;
import com.scrm.service.entity.PhoneAttribution;

import java.util.ArrayList;

public interface CustomerRestService {

    /**
     * 查询号码归属地
     * @param phone 号码
     * @return String
     */
    PhoneAttribution queryPhoneAttribution(String phone);

    /**
     * 扫描名片
     * @param url 图片地址
     * @return BusinessCard
     */
    BusinessCard scanBusinessCard(String url);

    /**
     * 查个人
     * @param keyword 关键字
     * @return Object[]
     */
    Object[] queryPersonal(String keyword);

    /**
     * 查企业
     * @param keyword 关键字
     * @return Object[]
     */
    Object[] queryCompany(String keyword);

    /**
     * 查企业详情
     * @param registerNo 工商注册号
     * @return Object[]
     */
    CompanyQXB queryCompanyDetail(String registerNo);

    /**
     * 查询企业间关系
     * @param firmA 企业1全称
     * @param firmB 企业2全称
     * @return ArrayList<ArrayList<FirmRelation>>
     */
    ArrayList<ArrayList<FirmRelation>> queryRelationBetweenFirm(String firmA, String firmB);
}
