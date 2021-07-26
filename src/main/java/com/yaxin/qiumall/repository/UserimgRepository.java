package com.yaxin.qiumall.repository;

import com.yaxin.qiumall.entity.Userimg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserimgRepository extends JpaRepository<Userimg, Integer> {
    List<Userimg> findUserimgsByUsername(String username);
    List<Userimg> findUserimgsByUserId(Integer userId);
}
