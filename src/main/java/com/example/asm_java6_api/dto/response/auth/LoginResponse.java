package com.example.asm_java6_api.dto.response.auth;

import com.example.asm_java6_api.dto.response.user.UserResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private UserResponse user;
    private String accessToken;
    private String refreshToken;
}
