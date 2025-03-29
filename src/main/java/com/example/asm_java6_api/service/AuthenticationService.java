package com.example.asm_java6_api.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.asm_java6_api.dto.request.auth.LoginRequest;
import com.example.asm_java6_api.dto.request.auth.RegisterRequest;
import com.example.asm_java6_api.dto.response.auth.LoginResponse;
import com.example.asm_java6_api.entity.User;
import com.example.asm_java6_api.exception.AppException;
import com.example.asm_java6_api.exception.ErrorCode;
import com.example.asm_java6_api.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtGeneratorService jwtGeneratorService;
    private final UserMapper userMapper;
    private final MailService mailService;

    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            User user = (User) authentication.getPrincipal();
            String accessToken = jwtGeneratorService.generateAccessToken(user);
            String refreshToken = jwtGeneratorService.generateRefreshToken(user);
            return LoginResponse.builder()
                    .user(null)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (Exception e) {
            throw new AppException(ErrorCode.EMAIL_OR_PASSWORD_INVALID);
        }
    }

    public void register(RegisterRequest request) {
        Map<String, String> models = new HashMap<>();
        models.put("name", "phuong");
        models.put("otp", "2343");
        models.put("expiryMinutes", "234");
        mailService.sendEmail("hchamtephuong@gmail.com", "Test", models, "otp-email");
    }
}
