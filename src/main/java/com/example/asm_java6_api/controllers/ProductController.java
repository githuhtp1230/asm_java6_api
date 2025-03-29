package com.example.asm_java6_api.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.asm_java6_api.dto.ApiResponse;
import com.example.asm_java6_api.dto.response.product.ProductResponse;
import com.example.asm_java6_api.dto.response.product.ProductReviewResponse;
import com.example.asm_java6_api.entity.ProductReview;
import com.example.asm_java6_api.service.ProductReviewService;
import com.example.asm_java6_api.service.ProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final ProductReviewService productReviewService;

    @GetMapping
    public ApiResponse<List<ProductResponse>> getProducts() {
        return ApiResponse.<List<ProductResponse>>builder()
                .code(200)
                .message("Get all products successfully")
                .data(productService.getProducts())
                .build();
    }

    @GetMapping("/{productId}")
    public ApiResponse<ProductResponse> getProduct(@PathVariable Long productId) {
        return ApiResponse.<ProductResponse>builder()
                .code(200)
                .message("Get product successfully")
                .data(productService.getProductById(productId))
                .build();
    }

    @GetMapping("/{productId}/reviews")
    public ApiResponse<List<ProductReviewResponse>> getReviewsByProduct(@PathVariable Long productId) {
        return ApiResponse.<List<ProductReviewResponse>>builder()
                .code(200)
                .message("Get reviews of product successully")
                .data(productReviewService.getReviewsByProduct(productId))
                .build();
    }

}
