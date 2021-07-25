package com.yaxin.qiumall.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Test
    void findall(){
        System.out.println(userRepository.findAll());
    }
    @Test
    void findByUsernameOrPhone(){
        try{
            System.out.println(userRepository.findUserByUsernameOrPhone("ya","186744209"));
        }catch (Exception e){
            System.out.println("异常！");
        }
    }
    @Test
    void findUserByUsernameOrPhoneAndAndPassword(){
        System.out.println(userRepository.findUserByUsernameOrPhone("yaxi", "18888888888"));
    }
}