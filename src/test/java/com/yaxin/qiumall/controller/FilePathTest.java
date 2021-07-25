package com.yaxin.qiumall.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.io.File;

@SpringBootTest
@Component
public class FilePathTest {
    @Value("${static.profile}")
    private String profile;

    @Test
    void findPath(){
        System.out.println(profile);
//        File file = new File("static", "userid");
//        if(!file.exists()){
//            System.out.println("不存在");
//            file.mkdirs();
//        }else{
//            System.out.println("存在");
//        }
//        System.out.println(file);
    }
}
