package com.example.asm_java6_api.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.asm_java6_api.entity.User;
import com.example.asm_java6_api.exception.AppException;
import com.example.asm_java6_api.exception.ErrorCode;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
public class JwtGeneratorService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final String ISSUER = "thephuong";

    @Value("${security.jwt.signer-key}")
    private String SIGNER_KEY;

    @Value("${security.jwt.access-token-valid-duration}")
    private Long ACCESS_TOKEN_VALID_DURATION;

    @Value("${security.jwt.refresh-token-valid-duration}")
    private Long REFRESH_TOKEN_VALID_DURATION;

    @Value("${redis.jwt-config.key-invalid-jwt-id}")
    private String KEY_INVALID_JWT_ID;

    public SignedJWT verifyToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

            if (!signedJWT.verify(verifier)) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }

            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            String jwtId = claimsSet.getJWTID();
            if (claimsSet.getExpirationTime().before(new Date())
                    || redisTemplate.opsForValue().get(KEY_INVALID_JWT_ID + jwtId) != null) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }

            return signedJWT;
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }

    public String generateAccessToken(User user) {
        try {
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getEmail())
                    .issuer(ISSUER)
                    .jwtID(UUID.randomUUID().toString())
                    .expirationTime(Date.from(Instant.now().plus(ACCESS_TOKEN_VALID_DURATION, ChronoUnit.MINUTES)))
                    .claim("userId", user.getId())
                    .build();

            JWSObject jwsObject = new JWSObject(
                    new JWSHeader(JWSAlgorithm.HS512),
                    new Payload(claimsSet.toJSONObject()));

            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (Exception e) {
            throw new AppException(ErrorCode.GENERATE_TOKEN_FAILED);
        }
    }

    public String generateRefreshToken(User user) {
        try {
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getEmail())
                    .issuer(ISSUER)
                    .jwtID(UUID.randomUUID().toString())
                    .expirationTime(Date.from(Instant.now().plus(REFRESH_TOKEN_VALID_DURATION, ChronoUnit.MINUTES)))
                    .claim("userId", user.getId())
                    .claim("isRefreshToken", true)
                    .build();

            JWSObject jwsObject = new JWSObject(
                    new JWSHeader(JWSAlgorithm.HS512),
                    new Payload(claimsSet.toJSONObject()));

            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (Exception e) {
            throw new AppException(ErrorCode.GENERATE_TOKEN_FAILED);
        }
    }

    public String extractJwtId(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getJWTID();
        } catch (Exception e) {
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }
    }
}
