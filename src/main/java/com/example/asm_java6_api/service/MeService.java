package com.example.asm_java6_api.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.asm_java6_api.dto.response.user.UserResponse;
import com.example.asm_java6_api.entity.User;
import com.example.asm_java6_api.mapper.UserMapper;
import com.example.asm_java6_api.utils.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MeService {
    private final UserMapper userMapper;

    public UserResponse getCurrentProfile() {
        return userMapper.toUserResponse(SecurityUtil.getCurrentUser());
    }
}
