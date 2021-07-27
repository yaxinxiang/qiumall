package com.yaxin.qiumall.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductimgRepositoryTest {
    @Autowired
    ProductimgRepository productimgRepository;

    @Test
    void test(){
        System.out.println(productimgRepository.findProductimgsBypId(1));
    }

}