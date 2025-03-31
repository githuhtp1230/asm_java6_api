package com.example.asm_java6_api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.asm_java6_api.dto.ApiResponse;
import com.example.asm_java6_api.dto.response.user.UserResponse;
import com.example.asm_java6_api.mapper.UserMapper;
import com.example.asm_java6_api.service.MeService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/me/profile")
public class ProfileController {
    private final MeService meService;

    @GetMapping
    public ApiResponse<UserResponse> getCurrentProfile() {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Get current profile successfully")
                .data(meService.getCurrentProfile())
                .build();
    }

}
