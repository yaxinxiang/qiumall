package com.yaxin.qiumall.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity//对应数据库实体
@Data//实现各种类方法
public class Product {
    @Id//表示主键
    @GeneratedValue(strategy= GenerationType.IDENTITY)//表示该键自增
    private Integer id;
    private String name;
    private Double price;
    private String category;
}
