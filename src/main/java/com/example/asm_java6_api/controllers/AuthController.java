package com.example.asm_java6_api.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.asm_java6_api.dto.ApiResponse;
import com.example.asm_java6_api.dto.request.auth.LoginRequest;
import com.example.asm_java6_api.dto.request.auth.RegisterRequest;
import com.example.asm_java6_api.dto.request.otp.RegisterOtpRequest;
import com.example.asm_java6_api.dto.response.auth.LoginResponse;
import com.example.asm_java6_api.dto.response.auth.RefreshTokenResponse;
import com.example.asm_java6_api.dto.response.user.UserResponse;
import com.example.asm_java6_api.service.AuthenticationService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@Service
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final HttpSession httpSession;
    private final HttpServletResponse httpServletResponse;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        return ApiResponse.<LoginResponse>builder()
                .code(200)
                .message("Login successfully")
                .data(authenticationService.login(request, httpServletResponse))
                .build();
    }

    @PostMapping("/register")
    public ApiResponse<?> register(@RequestBody @Valid RegisterRequest request) {
        authenticationService.register(request, httpSession);
        return ApiResponse.builder()
                .code(200)
                .message("OTP has been sent to your email, please verify OTP")
                .build();
    }

    @PostMapping("/refresh-token")
    public ApiResponse<RefreshTokenResponse> refreshToken(@CookieValue("refreshToken") String refreshToken,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        return ApiResponse.<RefreshTokenResponse>builder()
                .code(200)
                .message("Refresh token successfully")
                .data(authenticationService.refreshToken(refreshToken, authHeader))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<?> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        authenticationService.logout(authHeader, httpServletResponse);
        return ApiResponse.builder()
                .code(200)
                .message("Logout successfully")
                .build();
    }

    @PostMapping("/register-verify-otp")
    public ApiResponse<UserResponse> registerVerifyOtp(@RequestBody RegisterOtpRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Verify user successfully")
                .data(authenticationService.registerVerifyOtp(request, httpSession))
                .build();
    }

    @PostMapping("/register-resend-otp")
    public ApiResponse<UserResponse> registerResendOtp() {
        authenticationService.registerResendOtp(httpSession);
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Otp has been sent back to your email, please check your email")
                .build();
    }
}
