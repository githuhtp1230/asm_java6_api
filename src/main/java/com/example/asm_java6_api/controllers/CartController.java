package com.example.asm_java6_api.controllers;

import com.example.asm_java6_api.dto.ApiResponse;
import com.example.asm_java6_api.dto.request.cart.CartItemRequest;
import com.example.asm_java6_api.dto.response.cart.CartResponse;
import com.example.asm_java6_api.service.CartService;

import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final HttpSession httpSession;

    @GetMapping
    public ApiResponse<CartResponse> getCart() {
        return ApiResponse.<CartResponse>builder()
                .code(200)
                .message("Get cart successfully")
                .data(cartService.getCart(httpSession))
                .build();
    }

    @PostMapping
    public ApiResponse<?> saveItem(@RequestBody CartItemRequest request) {
        cartService.saveItem(request, httpSession);
        return ApiResponse.builder()
                .code(200)
                .message("Save item to cart successfully")
                .build();
    }

    @DeleteMapping("products/{productId}")
    public ApiResponse<?> removeItem(@PathVariable int productId) {
        cartService.removeItem(productId, httpSession);
        return ApiResponse.builder()
                .code(200)
                .message("Delete item from cart successfully")
                .build();
    }
}
