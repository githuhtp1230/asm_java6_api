package com.example.asm_java6_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.asm_java6_api.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}

