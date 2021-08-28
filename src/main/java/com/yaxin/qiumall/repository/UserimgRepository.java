package com.yaxin.qiumall.repository;

import com.yaxin.qiumall.entity.Userimg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserimgRepository extends JpaRepository<Userimg, Integer> {
    List<Userimg> findUserimgsByUsername(String username);
    List<Userimg> findUserimgsByUserId(Integer userId);
}
