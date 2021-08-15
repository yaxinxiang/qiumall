package com.yaxin.qiumall.controller;

import com.yaxin.qiumall.annotation.PassToken;
import com.yaxin.qiumall.entity.User;
import com.yaxin.qiumall.entity.Userimg;
import com.yaxin.qiumall.repository.UserRepository;
import com.yaxin.qiumall.repository.UserimgRepository;
import com.yaxin.qiumall.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserimgRepository userimgRepository;

//    @PassToken
//    @GetMapping("/register")
//    public Map<>

    @PassToken
    @ResponseBody
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
        String token = JwtUtil.sign(query);
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

    //获取user个人信息
    @ResponseBody
    @GetMapping("/selfinfo")
    public User getSelfInfo(@RequestHeader ("token") String token){
        Integer userId = JwtUtil.getUserIdByToken(token);
        return userRepository.findUserById(userId);
    }

    //获取用户个人头像
    @ResponseBody
    @GetMapping("/selfimg")
    public List<Userimg> getUserImg(@RequestHeader("token") String token){
        Integer userId = JwtUtil.getUserIdByToken(token);
        return userimgRepository.findUserimgsByUserId(userId);
    }

    //本接口只做测试使用
    @PassToken
    @ResponseBody
    @GetMapping("/findall")
    public List<User> findall(){
        return userRepository.findAll();
    }

    @PassToken
    @ResponseBody
    @GetMapping("/test")
    public Map<String, Object> test(){
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "后端项目运行正常");
        return map;
    }
}
