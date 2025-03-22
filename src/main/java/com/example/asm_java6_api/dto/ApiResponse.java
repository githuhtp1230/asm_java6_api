package com.example.asm_java6_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;
    private String logError;
}