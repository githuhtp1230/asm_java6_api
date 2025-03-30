package com.example.asm_java6_api.dto.request.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
}
