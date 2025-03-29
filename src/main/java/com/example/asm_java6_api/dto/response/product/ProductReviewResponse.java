package com.example.asm_java6_api.dto.response.product;

import com.example.asm_java6_api.dto.response.user.UserResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductReviewResponse {
    private Integer rating;
    private UserResponse user;
}
