package com.example.asm_java6_api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.asm_java6_api.dto.ApiResponse;
import com.example.asm_java6_api.dto.request.category.SaveCategoryRequest;
import com.example.asm_java6_api.dto.response.category.CategoryResponse;
import com.example.asm_java6_api.dto.response.product.ProductResponse;
import com.example.asm_java6_api.service.CategoryService;
import com.example.asm_java6_api.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final ProductService productService;

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getCategories() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .code(200)
                .message("Get categories successfully")
                .data(categoryService.getCategories())
                .build();
    }

    @GetMapping("/{categoryId}/products")
    public ApiResponse<List<ProductResponse>> getProductsByCategory(@PathVariable int categoryId) {
        return ApiResponse.<List<ProductResponse>>builder()
                .code(200)
                .message("Get categories successfully")
                .data(productService.getProductsByCategory(categoryId))
                .build();
    }

    @PostMapping
    public ApiResponse<CategoryResponse> saveCategory(@RequestBody @Valid SaveCategoryRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .code(200)
                .message("Save category successfully")
                .data(categoryService.saveCategory(request))
                .build();
    }

    @PostMapping("/{categoryId}/delete")
    public ApiResponse<?> deleteCategory(@PathVariable int categoryId) {
        categoryService.deleteCategory(categoryId);
        return ApiResponse.builder()
                .code(200)
                .message("Delete category successfully")
                .build();
    }
}
