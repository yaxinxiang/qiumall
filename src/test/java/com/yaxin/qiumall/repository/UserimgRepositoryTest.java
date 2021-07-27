package com.yaxin.qiumall.repository;

import com.yaxin.qiumall.entity.Userimg;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class UserimgRepositoryTest {
    @Autowired
    UserimgRepository userimgRepository;
    @Test
    void test(){
        System.out.println(userimgRepository.findAll());
        System.out.println(userimgRepository.findUserimgsByUsername("yaxin"));
        System.out.println(userimgRepository.findUserimgsByUserId(1));
    }

}