package com.example.asm_java6_api.configuration;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.asm_java6_api.entity.User;
import com.example.asm_java6_api.service.CartService;
import com.example.asm_java6_api.service.JwtGeneratorService;
import com.example.asm_java6_api.service.UserDetailsServiceImpl;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OncePerRequestFilterImpl extends OncePerRequestFilter {
    private final CartService cartService;
    private final JwtGeneratorService jwtGeneratorService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        cartService.setTimeOutCart(request.getSession());
        try {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(7);
            SignedJWT signedJWT = jwtGeneratorService.verifyToken(token);
            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();

            if (jwtClaimsSet.getBooleanClaim("isRefreshToken") != null) {
                filterChain.doFilter(request, response);
                return;
            }

            String email = jwtClaimsSet.getSubject();

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = (User) userDetailsServiceImpl.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user,
                        null, user.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
        }
    }
}
