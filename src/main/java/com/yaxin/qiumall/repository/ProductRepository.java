package com.yaxin.qiumall.repository;

import com.yaxin.qiumall.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
