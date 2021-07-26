package com.yaxin.qiumall.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Userimg {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)//表示该键自增
    private Integer id;
    private Integer userId;
    private String username;
    private String uImgUrl;

}
