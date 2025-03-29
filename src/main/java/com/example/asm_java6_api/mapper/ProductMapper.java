package com.example.asm_java6_api.mapper;

import com.example.asm_java6_api.dto.response.product.ProductImageResponse;
import com.example.asm_java6_api.dto.response.product.ProductResponse;
import com.example.asm_java6_api.dto.response.product.ProductReviewResponse;
import com.example.asm_java6_api.entity.Product;
import com.example.asm_java6_api.entity.ProductImage;
import com.example.asm_java6_api.entity.ProductReview;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toProductResponse(Product product);

    ProductImageResponse toProductImageResponse(ProductImage productImage);

    ProductReviewResponse toProductReviewResponse(ProductReview productReview);
}
