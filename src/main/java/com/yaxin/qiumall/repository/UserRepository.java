package com.yaxin.qiumall.repository;

import com.yaxin.qiumall.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByUsername(String username);
    User findUserByPhone(String phone);
    //根据用户名或者手机号码返回用户（数据库中的用户以及手机号码均是不能出现重复）
    User findUserByUsernameOrPhone(String username, String phone);
    User findUserById(Integer userId);
}
