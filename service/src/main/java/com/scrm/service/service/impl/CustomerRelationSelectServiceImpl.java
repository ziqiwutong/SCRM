package com.scrm.service.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.scrm.service.entity.RelationScore;
import com.scrm.service.service.CustomerRelationSelectService;
import com.scrm.service.service.CustomerRestService;
import com.scrm.service.vo.FirmRelation;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerRelationSelectServiceImpl implements CustomerRelationSelectService {
    @Resource
    private CustomerRestService customerRestService;

    @Override
    public ArrayList<ArrayList<FirmRelation>> queryRelationBetweenFirm(String firmA, String firmB) {
        return customerRestService.queryRelationBetweenFirm(firmA, firmB);
    }

    @Override
    public List<RelationScore> scoreRelationBetweenFirm(ArrayList<ArrayList<FirmRelation>> firmRelations) {
        //定义路径各个方面对总分的影响程度
        double countPercent = 0.5;  //路径节点数量所占权重
        double edgePercent = 0.5;  //路径关系所占权重
        double equityPercent = 0.4; //
        double employPercent = 0.4; //
        double attributePercent = 0.2; //

        ArrayList<RelationScore> firmRelationsWithScore = new ArrayList<>();
        for(ArrayList<FirmRelation> firmRelation : firmRelations){
            if(firmRelation.size() != 0){
                //计算路径在长度方面的分数
                int size = firmRelation.size();
                double countScore = 1 / (1 + (Math.exp(size - 5)));

                //计算路径在关系方面的分数
                //评价关系中的股东关系
                int EquityCountFlag = 0, EquityScoreFlag = 0;
                List<List<String>> labelEquityCategory = new ArrayList<>();
                labelEquityCategory.add(Arrays.asList("法定代表人"));
                labelEquityCategory.add(Arrays.asList("股东","十大流通股东","十大股东","新三板股东"));
                labelEquityCategory.add(Arrays.asList("历史股东"));
                ArrayList<Integer> linkLabelEquityScore = new ArrayList<>();
                for(FirmRelation Equityrelation : firmRelation){
                    if (Equityrelation.getLabel() == null) {
                        continue;
                    }
                    for(List<String> Category : labelEquityCategory){
                        for(String certainCategory : Category){
                            if(Equityrelation.getLabel().contains("，")){
                                String[] StrArray = Equityrelation.getLabel().split("，");
                                for(String str : StrArray){
                                    if(str.equals(certainCategory)){
                                        linkLabelEquityScore.add(100 - 20 * EquityScoreFlag);
                                        EquityCountFlag += 1;
                                        break;
                                    }
                                }
                                if(EquityCountFlag != 0){
                                    break;
                                }
                            }else{
                                if(Equityrelation.getLabel().equals(certainCategory)){
                                    linkLabelEquityScore.add(100 - 20 * EquityScoreFlag);
                                    EquityCountFlag += 1;
                                    break;
                                }
                            }
                        }
                        if(EquityCountFlag == 0){
                            EquityScoreFlag += 1;
                        }else{
                            break;
                        }
                    }
                    if(EquityCountFlag == 0){
                        linkLabelEquityScore.add(100 - 20 * EquityScoreFlag);
                        EquityScoreFlag = 0;
                    }else{
                        EquityCountFlag = 0;
                        EquityScoreFlag = 0;
                    }
                }

                //评价关系中的雇佣关系
                int EmployCountFlag = 0, EmployScoreFlag = 0;
                List<List<String>> labelEmployCategory = new ArrayList<>();
                labelEmployCategory.add(Arrays.asList("分支机构"));
                labelEmployCategory.add(Arrays.asList("董监高", "公告中的董监高"));
                labelEmployCategory.add(Arrays.asList("历史董监高"));
                ArrayList<Integer> linkLabelEmployScore = new ArrayList<>();
                for(FirmRelation Employrelation : firmRelation) {
                    if (Employrelation.getLabel() == null) {
                        continue;
                    }
                    for (List<String> Category : labelEmployCategory) {
                        for (String certainCategory : Category) {
                            if(Employrelation.getLabel().contains("，")){
                                String[] StrArray = Employrelation.getLabel().split("，");
                                for(String str : StrArray){
                                    if(str.equals(certainCategory)){
                                        linkLabelEmployScore.add(100 - 20 * EmployScoreFlag);
                                        EmployCountFlag += 1;
                                        break;
                                    }
                                }
                                if(EmployCountFlag != 0){
                                    break;
                                }
                            }else{
                                if(Employrelation.getLabel().equals(certainCategory)){
                                    linkLabelEmployScore.add(100 - 20 * EmployScoreFlag);
                                    EmployCountFlag += 1;
                                    break;
                                }
                            }
                        }
                        if (EmployCountFlag == 0) {
                            EmployScoreFlag += 1;
                        } else {
                            break;
                        }
                    }
                    if (EmployCountFlag == 0) {
                        linkLabelEmployScore.add(100 - 20 * EmployScoreFlag);
                        EmployScoreFlag = 0;
                    } else {
                        EmployCountFlag = 0;
                        EmployScoreFlag = 0;
                    }
                }

                //评价关系中的其他属性关系
                int AttributeCountFlag = 0;
                List<List<String>> labelAttributeCategory = new ArrayList<>();
                labelAttributeCategory.add(Arrays.asList("相同名字", "相同电话", "相同地址", "相同邮箱", "相同域名",
                        "相同软件著作权", "相同裁判文书", "相同专利", "相同字号", "相同域名关键字或相同邮箱关键字", "相同历史地址",
                        "相同历史邮箱", "相同历史电话", "相同历史域名"));
                ArrayList<Integer> linkLabelAttributeScore = new ArrayList<>();
                for(FirmRelation Attributerelation : firmRelation) {
                    if (Attributerelation.getLabel() == null) {
                        continue;
                    }
                    for (List<String> Category : labelAttributeCategory) {
                        for (String certainCategory : Category) {
                            if(Attributerelation.getLabel().contains("，")){
                                String[] StrArray = Attributerelation.getLabel().split("，");
                                for(String str : StrArray){
                                    if(str.equals(certainCategory)){
                                        AttributeCountFlag += 1;
                                    }
                                }
                            }else{
                                if(Attributerelation.getLabel().equals(certainCategory)){
                                    AttributeCountFlag += 1;
                                }
                            }
                        }
                    }
                    if (AttributeCountFlag == 0) {
                        linkLabelAttributeScore.add(40);
                    } else {
                        linkLabelAttributeScore.add((int) (AttributeCountFlag / (AttributeCountFlag + 0.5) * 100));
                        AttributeCountFlag = 0;
                    }
                }

                //计算关系方面总评分
                ArrayList<Double> linkLabelScore = new ArrayList<>();
                for(int i = 0; i < firmRelation.size() - 1; i++){
                    linkLabelScore.add(linkLabelEquityScore.get(i) * equityPercent +
                            linkLabelEmployScore.get(i) * employPercent +
                            linkLabelAttributeScore.get(i) * attributePercent);
                }
                double edgeScore = linkLabelScore.stream().collect(Collectors.averagingDouble(Double::doubleValue));

                //计算总分，同时将分数插入到路径之前一并返回
                double score = countScore * countPercent * 100 + edgeScore * edgePercent;
                RelationScore relationScore = new RelationScore();
                DecimalFormat df = new DecimalFormat("######0.00");
                relationScore.setScore(df.format(score));
                relationScore.setFirmRelation(firmRelation);
                firmRelationsWithScore.add(relationScore);
            }
        }
        return firmRelationsWithScore.stream().sorted(Comparator.comparing(RelationScore::getScore).reversed()).collect(Collectors.toList());
    }
}
