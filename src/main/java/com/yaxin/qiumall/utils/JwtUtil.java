package com.yaxin.qiumall.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.yaxin.qiumall.entity.User;
import org.springframework.context.annotation.Bean;

import java.util.Date;

public class JwtUtil {

    //token过期时间一周
    public static final long EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000;

    public static String sign(User user){
        Integer userId = user.getId();
        String username = user.getUsername();
        String password = user.getPassword();
        //过期的时间
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(password);
        return JWT.create()
                .withClaim("userId", userId)
                .withClaim("username", username)
                .withExpiresAt(date)
                .sign(algorithm);
    }


    public static boolean verify(String token, User user){
        Integer userId = user.getId();
        String username = user.getUsername();
        String password = user.getPassword();
        try{
            //加密算法
            Algorithm algorithm = Algorithm.HMAC256(password);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("userId", userId)
                    .withClaim("username", username)
                    .build();
            //校验token
            DecodedJWT decodedJWT = verifier.verify(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static String getUserNameByToken(String token)  {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("username")
                .asString();
    }

    public static Integer getUserIdByToken(String token)  {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("userId")
                .asInt();
    }

}
