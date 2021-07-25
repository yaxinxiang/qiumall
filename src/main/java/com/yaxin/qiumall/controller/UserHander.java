package com.yaxin.qiumall.controller;

import com.yaxin.qiumall.annotation.PassToken;
import com.yaxin.qiumall.annotation.UserLoginToken;
import com.yaxin.qiumall.entity.User;
import com.yaxin.qiumall.repository.UserRepository;
import com.yaxin.qiumall.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserHander {

    @Autowired
    private UserRepository userRepository;

//    @PassToken
//    @GetMapping("/register")
//    public Map<>

    @PassToken
    @GetMapping("/login")
    public Map<String, Object> login(@RequestBody User user){
        Map<String, Object> map = new HashMap<>();
        String username = user.getUsername();
        String phone = user.getPhone();
        String password = user.getPassword();
        //账号密码校验
        User query = userRepository.findUserByUsernameOrPhone(username, phone);
        if(query == null || !query.getPassword().equals(password)){
            map.put("code", 403);
            map.put("msg", "账户或密码错误");
            return map;
        }
        //验证通过后创建token
        String token = JwtUtil.sign(query.getUsername(),query.getPassword());
        if(token == null){
            map.put("code", 403);
            map.put("msg", "申请token失败");
        }else{
            map.put("code", 200);
            map.put("msg", "认证成功");
            map.put("token", token);
        }
        return map;
    }

    //本接口只做测试使用
    @GetMapping("/findall")
    public List<User> findall(){
        return userRepository.findAll();
    }
    @PassToken
    @GetMapping("/test")
    public String test(){
        return "success1";
    }
}
