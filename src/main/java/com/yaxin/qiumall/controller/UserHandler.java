package com.yaxin.qiumall.controller;

import com.yaxin.qiumall.annotation.PassToken;
import com.yaxin.qiumall.entity.User;
import com.yaxin.qiumall.entity.Userimg;
import com.yaxin.qiumall.repository.UserRepository;
import com.yaxin.qiumall.repository.UserimgRepository;
import com.yaxin.qiumall.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserHandler {

    HttpHeaders headers;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserimgRepository userimgRepository;

    @Autowired
    private JwtUtil jwtUtil;

//    @PassToken
//    @GetMapping("/register")
//    public Map<>

    @PassToken
    @GetMapping("/login")
    public ResponseEntity<User> loginNew(@RequestBody User user){
        User query;
        String username = user.getUsername();
        String phone = user.getPhone();
        String password = user.getPassword();

        //账号密码不能为空
        if((username == null && password == null) || (phone == null && password == null)){
            //401:身份验证失败
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        //账号密码校验
        if(username != null){
            query = userRepository.findUserByUsernameAndPassword(username, password);
        }else{
            query = userRepository.findUserByPhoneAndPassword(phone, password);
        }
        if(query == null){
            //401:身份验证失败
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        //验证通过后创建token
        String token = jwtUtil.sign(query);
        if(token == null){
            //500
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        headers = new HttpHeaders();
        headers.add("token", token);

        //200
        return new ResponseEntity<>(query, headers, HttpStatus.OK);
    }

    //获取user个人信息
    @GetMapping("/selfinfo")
    public ResponseEntity<User> getSelfInfo(@RequestHeader ("token") String token){
        User query = jwtUtil.getDBUSerByToken(token);
        return new ResponseEntity<>(query, HttpStatus.OK);
    }

    //获取用户个人头像
    @GetMapping("/selfimg")
    public ResponseEntity<List<Userimg>> getUserImg(@RequestHeader("token") String token){
        Integer userId = jwtUtil.getUserIdByToken(token);
        return new ResponseEntity<>(userimgRepository.findUserimgsByUserId(userId), HttpStatus.OK);
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
        map.put("msg", "后端项目运行正常");
        return map;
    }
}
