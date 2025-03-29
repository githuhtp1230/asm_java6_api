package com.example.asm_java6_api.repository;

import com.example.asm_java6_api.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByName(String name);

    List<Category> findCategoriesByIsActiveIsTrue();
}
