package com.yaxin.qiumall.utils;

import com.yaxin.qiumall.entity.User;
import org.junit.jupiter.api.Test;

class JwtUtilTest {

    @Test
    void sign() {
        User u = new User();
        u.setId(1);
        u.setUsername("yaxin");
        u.setPassword("123456");
        String token = JwtUtil.sign(u);
        System.out.println(token);
        System.out.println(JwtUtil.verify(token,u));
        Integer userId = JwtUtil.getUserIdByToken(token);
        System.out.println(userId);
    }
}