package com.example.asm_java6_api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.asm_java6_api.dto.ApiResponse;
import com.example.asm_java6_api.dto.response.category.CategoryResponse;
import com.example.asm_java6_api.dto.response.product.ProductResponse;
import com.example.asm_java6_api.service.CategoryService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getCatetgories() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .message("Get categories successfully")
                .data(categoryService.getCategories())
                .build();
    }

    @GetMapping("/{categoryId}/products")
    public ApiResponse<List<ProductResponse>> getProductsByCategory(@RequestParam String param) {
        // return new String();
    }

}
