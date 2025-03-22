package com.example.asm_java6_api.controllers;

import com.example.asm_java6_api.dto.ApiResponse;
import com.example.asm_java6_api.dto.request.cart.CartItemRequest;
import com.example.asm_java6_api.dto.response.cart.CartResponse;
import com.example.asm_java6_api.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/cart")
public class CartController {
    CartService cartService;

    @GetMapping
    public ApiResponse<CartResponse> getCart() {
        return ApiResponse.<CartResponse>builder()
                .code(200)
                .message("Get cart successfully")
                .data(cartService.getCart())
                .build();
    }

    @PostMapping
    public String saveItem(@RequestBody CartItemRequest request) {
        cartService.saveItem(request);
        return "OK";
    }

    @DeleteMapping("products/{productId}")
    public String removeItem(@PathVariable int productId) {
        cartService.removeItem(productId);
        return "OK";
    }
}
