package com.example.asm_java6_api.service;

import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.example.asm_java6_api.dto.request.category.SaveCategoryRequest;
import com.example.asm_java6_api.dto.response.category.CategoryResponse;
import com.example.asm_java6_api.entity.Category;
import com.example.asm_java6_api.exception.AppException;
import com.example.asm_java6_api.exception.ErrorCode;
import com.example.asm_java6_api.mapper.CategoryMapper;
import com.example.asm_java6_api.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponse> getCategories() {
        List<Category> categories = categoryRepository.findCategoriesByIsActiveIsTrue();
        return categories.stream().map(categoryMapper::toCategoryResponse).toList();
    }

    public CategoryResponse saveCategory(SaveCategoryRequest request) {
        Category category = categoryRepository.findByName(request.getName());
        if (category != null && category.getIsActive()) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }
        if (category != null && !category.getIsActive()) {
            category.setIsActive(true);
            categoryRepository.save(category);
            return categoryMapper.toCategoryResponse(category);
        }
        Category categorySave = categoryRepository.save(categoryMapper.toCategory(request));
        return categoryMapper.toCategoryResponse(categorySave);
    }

    public void deleteCategory(int categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        category.setIsActive(false);
        categoryRepository.save(category);
    }
}
