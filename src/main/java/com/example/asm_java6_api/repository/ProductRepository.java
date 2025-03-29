package com.example.asm_java6_api.repository;

import com.example.asm_java6_api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findProductsByCategoryId(Integer categoryId);

    List<Product> findProductsByIsActiveIsTrue();
}
