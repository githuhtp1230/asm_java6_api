package com.example.asm_java6_api.service;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.asm_java6_api.dto.request.auth.LoginRequest;
import com.example.asm_java6_api.dto.request.auth.RegisterRequest;
import com.example.asm_java6_api.dto.request.otp.RegisterOtpRequest;
import com.example.asm_java6_api.dto.response.auth.LoginResponse;
import com.example.asm_java6_api.dto.response.auth.RefreshTokenResponse;
import com.example.asm_java6_api.dto.response.user.UserResponse;
import com.example.asm_java6_api.entity.User;
import com.example.asm_java6_api.exception.AppException;
import com.example.asm_java6_api.exception.ErrorCode;
import com.example.asm_java6_api.mapper.UserMapper;
import com.example.asm_java6_api.repository.UserRepository;
import com.nimbusds.jwt.SignedJWT;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtGeneratorService jwtGeneratorService;
    private final MailService mailService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private HashOperations<String, String, Object> hashOperations;

    @Value("${redis.register-otp-config.key-pending-user}")
    private String KEY_REGISTER_PENDING_USER;

    @Value("${redis.register-otp-config.key-otp}")
    private String KEY_REGISTER_OTP;

    @Value("${redis.register-otp-config.key-duration-resend-otp}")
    private String KEY_DURATION_RESEND_OTP;

    @Value("${redis.jwt-config.key-invalid-jwt-id}")
    private String KEY_INVALID_JWT_ID;

    @Value("${redis.register-otp-config.expiration-time-pending-user}")
    private Long EXPIRATION_TIME_PENDING_USER;

    @Value("${redis.register-otp-config.expiration-time-otp}")
    private Long EXPIRATION_TIME_OTP;

    @Value("${redis.register-otp-config.expiration-time-resend-otp}")
    private Long EXPIRATION_TIME_RESEND_OTP;

    @Value("${security.jwt.access-token-valid-duration}")
    private Long ACCESS_TOKEN_VALID_DURATION;

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    public LoginResponse login(LoginRequest request, HttpServletResponse httpServletResponse) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            User user = (User) authentication.getPrincipal();
            String accessToken = jwtGeneratorService.generateAccessToken(user);
            String refreshToken = jwtGeneratorService.generateRefreshToken(user);

            Cookie cookie = new Cookie("refreshToken", refreshToken);
            cookie.setHttpOnly(false);
            cookie.setSecure(false);
            cookie.setDomain("localhost");
            cookie.setPath("/api/auth/");
            cookie.setMaxAge(14 * 24 * 60 * 60);

            httpServletResponse.addCookie(cookie);

            return LoginResponse.builder()
                    .user(userMapper.toUserResponse(user))
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (Exception e) {
            throw new AppException(ErrorCode.EMAIL_OR_PASSWORD_INVALID);
        }
    }

    public void register(RegisterRequest request, HttpSession httpSession) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user != null) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        String keyPendingUser = KEY_REGISTER_PENDING_USER + httpSession.getId();
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        hashOperations.put(keyPendingUser, "name", request.getName());
        hashOperations.put(keyPendingUser, "email", request.getEmail());
        hashOperations.put(keyPendingUser, "password", encodedPassword);
        redisTemplate.expire(keyPendingUser, EXPIRATION_TIME_PENDING_USER, TimeUnit.MINUTES);

        String otp = generateOtp();
        String keyOtp = KEY_REGISTER_OTP + httpSession.getId();
        redisTemplate.opsForValue().set(keyOtp, otp, EXPIRATION_TIME_OTP, TimeUnit.MINUTES);

        Map<String, String> models = new HashMap<>();
        models.put("otp", otp);
        models.put("expiryMinutes", EXPIRATION_TIME_OTP + "");
        mailService.sendEmail(request.getEmail(), "Xác nhận đăng ký email", models,
                "otp-email");
    }

    public void registerResendOtp(HttpSession httpSession) {
        String keyPendingUser = KEY_REGISTER_PENDING_USER + httpSession.getId();
        String keyDurationResendOtp = KEY_DURATION_RESEND_OTP + httpSession.getId();
        if (redisTemplate.opsForValue().get(keyDurationResendOtp) != null) {
            throw new AppException(ErrorCode.TOO_MANY_REQUEST_RESEND_OTP);
        }
        if (hashOperations.keys(keyPendingUser).size() == 0) {
            throw new AppException(ErrorCode.REGISTAION_TIME_EXPIRED);
        }

        String otp = generateOtp();
        String keyOtp = KEY_REGISTER_OTP + httpSession.getId();
        redisTemplate.opsForValue().set(keyOtp, otp, EXPIRATION_TIME_OTP, TimeUnit.MINUTES);

        redisTemplate.opsForValue().set(keyDurationResendOtp, "", EXPIRATION_TIME_RESEND_OTP, TimeUnit.MINUTES);

        Map<String, String> models = new HashMap<>();
        models.put("otp", otp);
        models.put("expiryMinutes", EXPIRATION_TIME_OTP + "");
        mailService.sendEmail(hashOperations.get(keyPendingUser, "email").toString(), "Xác nhận đăng ký email", models,
                "otp-email");
    }

    public UserResponse registerVerifyOtp(RegisterOtpRequest request, HttpSession httpSession) {
        String keyOtp = KEY_REGISTER_OTP + httpSession.getId();
        String keyPendingUser = KEY_REGISTER_PENDING_USER + httpSession.getId();
        String keyDurationResendOtp = KEY_DURATION_RESEND_OTP + httpSession.getId();
        Object otpObj = redisTemplate.opsForValue().get(keyOtp);
        if (hashOperations.keys(keyPendingUser).size() == 0) {
            throw new AppException(ErrorCode.REGISTAION_TIME_EXPIRED);
        }
        if (otpObj == null) {
            throw new AppException(ErrorCode.OTP_EXPIRED);
        }
        String otp = otpObj.toString();
        if (!request.getOtp().equals(otp)) {
            throw new AppException(ErrorCode.OTP_INVALID);
        }
        User user = new User();
        user.setName(hashOperations.get(keyPendingUser, "name").toString());
        user.setEmail(hashOperations.get(keyPendingUser, "email").toString());
        user.setPassword(hashOperations.get(keyPendingUser, "password").toString());
        user = userRepository.save(user);

        redisTemplate.delete(keyOtp);
        redisTemplate.delete(keyPendingUser);
        redisTemplate.delete(keyDurationResendOtp);

        return userMapper.toUserResponse(user);
    }

    public RefreshTokenResponse refreshToken(String refreshToken, String bearerToken) {
        try {
            String token = bearerToken.substring("Bearer ".length());
            String jwtId = jwtGeneratorService.extractJwtId(token);
            if (refreshToken.isBlank()) {
                throw new AppException(ErrorCode.REFRESH_TOKEN_INVALID);
            }
            SignedJWT signedJWT = jwtGeneratorService.verifyToken(refreshToken);
            String email = signedJWT.getJWTClaimsSet().getSubject();
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new AppException(ErrorCode.USER_NOT_FOUND);
            }
            redisTemplate.opsForValue().set(KEY_INVALID_JWT_ID + jwtId, token, ACCESS_TOKEN_VALID_DURATION,
                    TimeUnit.MINUTES);
            String accessToken = jwtGeneratorService.generateAccessToken(user);
            return RefreshTokenResponse.builder()
                    .accessToken(accessToken)
                    .build();
        } catch (Exception e) {
            throw new AppException(ErrorCode.REFRESH_TOKEN_INVALID);
        }
    }

    public void logout(String bearerToken, HttpServletResponse httpServletResponse) {
        try {
            String token = bearerToken.substring("Bearer ".length());
            String jwtId = jwtGeneratorService.extractJwtId(token);
            redisTemplate.opsForValue().set(KEY_INVALID_JWT_ID + jwtId, token, ACCESS_TOKEN_VALID_DURATION,
                    TimeUnit.MINUTES);
            deleteRefreshTokenCookie(httpServletResponse);
        } catch (Exception e) {
            throw new AppException(ErrorCode.LOGOUT_FAILED);
        }
    }

    private String generateOtp() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(1000, 10000));
    }

    private void deleteRefreshTokenCookie(HttpServletResponse httpServletResponse) {
        Cookie cookie = new Cookie("refreshToken", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/auth");
        cookie.setDomain("localhost");
        cookie.setMaxAge(0);
        httpServletResponse.addCookie(cookie);
    }
}
