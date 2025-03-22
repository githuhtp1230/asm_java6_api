package com.example.asm_java6_api.mapper;

import com.example.asm_java6_api.dto.response.product.ProductResponse;
import com.example.asm_java6_api.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toProductResponse(Product product);
}
