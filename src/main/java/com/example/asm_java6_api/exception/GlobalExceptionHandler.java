package com.example.asm_java6_api.exception;

import com.example.asm_java6_api.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.function.Function;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<?>> handlingAppException(AppException e) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(e.getErrorCode().getStatusCode().value())
                .message(e.getErrorCode().getMessage())
                .logError(e.getMessage())
                .build();

        return ResponseEntity
                .status(e.getErrorCode().getStatusCode())
                .body(apiResponse);
    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<?>> handlingRuntimeException(Exception e) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(ErrorCode.INTERNAL_SERVER_ERROR.getStatusCode().value())
                .message("Internal Server Error")
                .logError(e.getMessage())
                .build();

        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<?>> handlingMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<String> errors = fieldErrors.stream().map(new Function<FieldError, String>() {
            @Override
            public String apply(FieldError fieldError) {
                return fieldError.getDefaultMessage();
            }
        }).toList();

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(400)
                .message(errors.size() > 1 ? errors.toString() : errors.get(0))
                .build();

        return ResponseEntity.badRequest().body(apiResponse);
    }
}
