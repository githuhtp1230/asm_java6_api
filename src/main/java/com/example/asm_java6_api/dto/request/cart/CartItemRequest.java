package com.example.asm_java6_api.dto.request.cart;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemRequest {
    private Long productId;
}
