package com.example.asm_java6_api.mapper;

import org.mapstruct.Mapper;

import com.example.asm_java6_api.dto.request.auth.RegisterRequest;
import com.example.asm_java6_api.dto.response.user.UserResponse;
import com.example.asm_java6_api.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);

    User toUser(RegisterRequest request);
}
