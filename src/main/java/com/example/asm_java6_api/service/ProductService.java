package com.example.asm_java6_api.service;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.asm_java6_api.dto.response.product.ProductImageResponse;
import com.example.asm_java6_api.dto.response.product.ProductResponse;
import com.example.asm_java6_api.entity.Product;
import com.example.asm_java6_api.entity.ProductImage;
import com.example.asm_java6_api.exception.AppException;
import com.example.asm_java6_api.exception.ErrorCode;
import com.example.asm_java6_api.mapper.CategoryMapper;
import com.example.asm_java6_api.mapper.ProductMapper;
import com.example.asm_java6_api.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductResponse> getProducts() {
        List<Product> products = productRepository.findProductsByIsActiveIsTrue();
        return products.stream().map(productMapper::toProductResponse).toList();
    }

    public List<ProductResponse> getProductsByCategory(int categoryId) {
        List<Product> products = productRepository.findProductsByCategoryId(categoryId);
        return products.stream().map(productMapper::toProductResponse).toList();
    }

    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        return productMapper.toProductResponse(product);
    }
}
