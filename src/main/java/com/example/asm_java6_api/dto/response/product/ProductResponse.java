package com.example.asm_java6_api.dto.response.product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.example.asm_java6_api.dto.response.category.CategoryResponse;
import com.example.asm_java6_api.entity.Discount;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private Integer id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private String brand;
    private Set<ProductImageResponse> productImages;
    private CategoryResponse category;
}
