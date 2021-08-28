package com.yaxin.qiumall.repository;

import com.yaxin.qiumall.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserById(Integer userId);
    User findUserByUsername(String username);
    User findUserByPhone(String phone);
    User findUserByUsernameAndPassword(String username, String password);
    User findUserByPhoneAndPassword(String phone, String password);
    User findUserByIdAndUsername(Integer id, String username);
}
