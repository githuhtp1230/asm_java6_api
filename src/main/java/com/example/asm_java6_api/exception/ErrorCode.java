package com.example.asm_java6_api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {
    // auth
    LOGOUT_FAILED(HttpStatus.BAD_REQUEST, "Logout failed"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "You have not permission"),
    GENERATE_TOKEN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Generate token failed"),
    OTP_EXPIRED(HttpStatus.BAD_REQUEST, "Otp has expired, please try again"),
    REGISTAION_TIME_EXPIRED(HttpStatus.BAD_REQUEST, "Registation time has expired, please try again"),
    TOO_MANY_REQUEST_RESEND_OTP(HttpStatus.TOO_MANY_REQUESTS, "Please wait 1 minute to resend code"),
    // validate
    EMAIL_INVALID(HttpStatus.BAD_REQUEST, "Email is invalid"),
    USERNAME_INVALID(HttpStatus.BAD_REQUEST, "Username is invalid"),
    PASSWORD_INVALID(HttpStatus.BAD_REQUEST, "Password is invalid"),
    TOKEN_INVALID(HttpStatus.BAD_REQUEST, "Token is invalid"),
    EMAIL_OR_PASSWORD_INVALID(HttpStatus.BAD_REQUEST, "Email or password is invalid"),
    REFRESH_TOKEN_INVALID(HttpStatus.BAD_REQUEST, "Refresh token is invalid"),
    OTP_INVALID(HttpStatus.BAD_REQUEST, "Otp is invalid"),
    EMAIL_EXISTED(HttpStatus.CONFLICT, "Email already exists"),
    CATEGORY_EXISTED(HttpStatus.CONFLICT, "Category name already exists"),
    // not found
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Category not found"),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "Product not found"),
    // server
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");

    private HttpStatusCode statusCode;
    private String message;
}
