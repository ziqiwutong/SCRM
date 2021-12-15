package com.scrm.service.service;

import com.scrm.service.entity.RelationScore;
import com.scrm.service.vo.FirmRelation;

import java.util.ArrayList;
import java.util.List;

public interface CustomerRelationSelectService {
    ArrayList<ArrayList<FirmRelation>> queryRelationBetweenFirm(String firmA, String firmB);

    List<RelationScore> scoreRelationBetweenFirm(ArrayList<ArrayList<FirmRelation>> firmRelations);
}
