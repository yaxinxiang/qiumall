package com.yaxin.qiumall.repository;

import com.yaxin.qiumall.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired//自动装载，接口框架实现
    ProductRepository productRepository;

    @Test
    void findall(){
        System.out.println(productRepository.findAll());
    }

    @Test
    void save(){
        System.out.println(productRepository.findProductsByCategory("干果"));
    }
}