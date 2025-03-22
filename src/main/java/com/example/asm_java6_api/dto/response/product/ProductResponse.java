package com.example.asm_java6_api.dto.response.product;
import com.example.asm_java6_api.dto.response.category.CategoryResponse;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private String image;
    private CategoryResponse category;
}
