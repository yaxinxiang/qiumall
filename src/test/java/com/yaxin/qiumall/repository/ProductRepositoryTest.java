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
        Product product = new Product();
        product.setName("橘子");
        product.setPrice(10.0);
        product.setCategory("水果");
        Product product1 = productRepository.save(product);
        System.out.println(product1);
    }
}