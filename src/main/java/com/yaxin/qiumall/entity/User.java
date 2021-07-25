package com.yaxin.qiumall.entity;

import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Repository;

import javax.persistence.*;

@Entity//与数据库表单对应的实体类
@Data
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)//表示该键自增
    private Integer id;
    @Column(unique=true)
    private String username;
    private String password;
    @Column(unique=true)
    private String phone;
    private Integer sex;
}
