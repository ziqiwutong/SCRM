package com.scrm.service.service;

import com.scrm.service.vo.FirmRelation;

import java.util.ArrayList;

public interface CustomerRestService {

    /**
     * 查询企业间关系
     * @param firmA 企业1全称
     * @param firmB 企业2全称
     * @return ArrayList<ArrayList<FirmRelation>>
     */
    ArrayList<ArrayList<FirmRelation>> queryRelationBetweenFirm(String firmA, String firmB);
}
