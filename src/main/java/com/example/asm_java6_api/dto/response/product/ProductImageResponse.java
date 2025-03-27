package com.example.asm_java6_api.dto.response.product;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageResponse {
    private Integer id;
    private String url;
    private boolean isPrimary;
}
