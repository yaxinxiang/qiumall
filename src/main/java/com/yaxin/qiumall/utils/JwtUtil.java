package com.yaxin.qiumall.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.yaxin.qiumall.entity.User;
import com.yaxin.qiumall.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtUtil {

    @Autowired
    private UserRepository userRepository;
    //token过期时间一周
    public static final long EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000;

    public String sign(User user){
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


    public boolean verify(String token, User user){
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

    public String getUserNameByToken(String token)  {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("username")
                .asString();
    }

    public Integer getUserIdByToken(String token)  {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("userId")
                .asInt();
    }

    /**
     * 通过获取token中的id 和 用户名 判断用户是否存在
     * @param token token
     * @return true or false
     */
    public Boolean isExistInDB(String token){
        Integer userId = getUserIdByToken(token);
        String username = getUserNameByToken(token);
        User query = userRepository.findUserByIdAndUsername(userId, username);
        return query != null;
    }

    /**
     * 通过获取token中的id 和 用户名 查询数据库中的用户
     * tip:只有登陆后才能使用此方法
     * @param token token
     * @return User
     */
    public User getDBUSerByToken(String token){
        Integer userId = getUserIdByToken(token);
        String username = getUserNameByToken(token);
        return userRepository.findUserByIdAndUsername(userId, username);
    }
}
