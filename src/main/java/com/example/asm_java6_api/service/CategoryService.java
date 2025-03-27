package com.example.asm_java6_api.service;

import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.example.asm_java6_api.dto.response.category.CategoryResponse;
import com.example.asm_java6_api.entity.Category;
import com.example.asm_java6_api.mapper.CategoryMapper;
import com.example.asm_java6_api.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponse> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(new Function<Category, CategoryResponse>() {
            @Override
            public CategoryResponse apply(Category c) {
                return categoryMapper.toCategoryResponse(c);
            }
        }).toList();
    }
}
