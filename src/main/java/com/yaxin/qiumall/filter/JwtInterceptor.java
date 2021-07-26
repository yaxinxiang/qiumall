package com.yaxin.qiumall.filter;

import com.yaxin.qiumall.annotation.PassToken;
import com.yaxin.qiumall.annotation.UserLoginToken;
import com.yaxin.qiumall.entity.User;
import com.yaxin.qiumall.repository.UserRepository;
import com.yaxin.qiumall.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("token");
        //没有映射到方法直接通过
        if(!(handler instanceof HandlerMethod)){
            System.out.println("非映射到方法跳过认证");
            return true;
        }

        HandlerMethod handlerMethod=(HandlerMethod)handler;
        Method method=handlerMethod.getMethod();

        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                System.out.println("PassToken 注解跳过认证");
                return true;
            }
        }
        //token认证
        if(token == null){
            System.out.println("token为空!!!!!!!");
            return false;
        }
        String username = JwtUtil.getUserNameByToken(token);
        User user = userRepository.findUserByUsername(username);
        if(user == null){
            System.out.println("token验证失败");
            return false;
        }
        boolean result = JwtUtil.verify(token, user);
        if(!result){
            System.out.println("token验证失败");
            return false;
        }
        System.out.println("通过认证");
        return true;

    }
}
