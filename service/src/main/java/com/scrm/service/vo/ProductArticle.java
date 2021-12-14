package com.scrm.service.vo;

import lombok.Data;

@Data
public class ProductArticle {

    private Long id;

    private String productIdList;

    private String productName;

    private String articleContext;

    private String productImage;
}
