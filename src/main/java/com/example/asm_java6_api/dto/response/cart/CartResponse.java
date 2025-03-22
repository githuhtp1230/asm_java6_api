package com.example.asm_java6_api.dto.response.cart;
import java.util.List;

import com.example.asm_java6_api.dto.response.product.ProductResponse;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponse {
    Integer total;
    List<CartItemResponse> items;
}
