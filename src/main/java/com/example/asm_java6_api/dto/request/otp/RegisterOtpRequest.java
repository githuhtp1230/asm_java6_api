package com.example.asm_java6_api.dto.request.otp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterOtpRequest {
    private String otp;
}
