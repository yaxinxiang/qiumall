package com.yaxin.qiumall.repository;

import com.yaxin.qiumall.entity.Productimg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductimgRepository extends JpaRepository<Productimg, Integer> {
    List<Productimg> findProductimgsBypId(Integer PId);
}
