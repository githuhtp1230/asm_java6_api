package com.example.asm_java6_api.exception;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppException extends RuntimeException {
    private ErrorCode errorCode;
}