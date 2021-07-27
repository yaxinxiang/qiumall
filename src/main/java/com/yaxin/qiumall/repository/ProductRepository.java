package com.yaxin.qiumall.repository;

import com.yaxin.qiumall.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findProductsByCategory(String category);
}
