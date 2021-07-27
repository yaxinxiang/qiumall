package com.yaxin.qiumall.repository;

import com.yaxin.qiumall.entity.Productimg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductimgRepository extends JpaRepository<Productimg, Integer> {
    List<Productimg> findProductimgsBypId(Integer PId);
}
