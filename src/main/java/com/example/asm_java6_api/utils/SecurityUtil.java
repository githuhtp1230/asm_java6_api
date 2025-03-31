package com.example.asm_java6_api.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.asm_java6_api.entity.User;

public class SecurityUtil {
    private SecurityUtil() {
    };

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getPrincipal());
        return (User) authentication.getPrincipal();
    }
}
