package com.yaxin.qiumall.interceptor;

import com.yaxin.qiumall.annotation.PassToken;
import com.yaxin.qiumall.entity.User;
import com.yaxin.qiumall.repository.UserRepository;
import com.yaxin.qiumall.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 确保token有效（token验证正确，且数据库中存在）
 */
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //记录请求的时间
        System.out.print(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()) + "  INFO 网络请求记录: ");

        String token = request.getHeader("token");
        //没有映射到方法直接通过
        if(!(handler instanceof HandlerMethod)){
            System.out.println("非映射到方法跳过认证");
            return true;
        }

        HandlerMethod handlerMethod=(HandlerMethod)handler;
        Method method=handlerMethod.getMethod();

        //检查是否有PassToken注释，有则跳过认证
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
            response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
            return false;
        }
        String username = jwtUtil.getUserNameByToken(token);
        User user = userRepository.findUserByUsername(username);
        if(user == null){
            System.out.println("token验证失败");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        boolean result = jwtUtil.verify(token, user);
        if(!result){
            System.out.println("token验证失败");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
       if(!jwtUtil.isExistInDB(token)){
           System.out.println("用户不存在，尝试重新登陆获取新token！");
           response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
       }
        System.out.println("通过认证");
        return true;
    }
}
