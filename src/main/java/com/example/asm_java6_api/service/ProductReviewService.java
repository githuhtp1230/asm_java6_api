package com.example.asm_java6_api.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.asm_java6_api.dto.response.product.ProductReviewResponse;
import com.example.asm_java6_api.entity.Product;
import com.example.asm_java6_api.entity.ProductReview;
import com.example.asm_java6_api.exception.AppException;
import com.example.asm_java6_api.exception.ErrorCode;
import com.example.asm_java6_api.mapper.ProductMapper;
import com.example.asm_java6_api.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductReviewService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductReviewResponse> getReviewsByProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        return product.getProductReviews().stream().map(productMapper::toProductReviewResponse).toList();
    }
}
