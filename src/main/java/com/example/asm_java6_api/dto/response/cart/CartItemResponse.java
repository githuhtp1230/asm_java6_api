package com.example.asm_java6_api.dto.response.cart;

import com.example.asm_java6_api.dto.response.product.ProductResponse;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponse {
    ProductResponse product;
    Integer quantity;
}
