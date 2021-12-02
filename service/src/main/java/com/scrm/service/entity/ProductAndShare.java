package com.scrm.service.entity;

import lombok.Data;

/**
 * @author Ganyunhui
 * @create 2021-12-02 10:48
 */
@Data
public class ProductAndShare extends Product{
    private String username;
    private String userIcon;
    private String telephone;
}
