package com.yaxin.qiumall.utils;

import org.junit.jupiter.api.Test;

class JwtUtilTest {

    @Test
    void sign() {
        String token = JwtUtil.sign("yaxin", "123456");
        System.out.println(token);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(JwtUtil.verify(token, "yaxin", "123456"));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(JwtUtil.verify(token, "yaxin", "123456"));
    }
}